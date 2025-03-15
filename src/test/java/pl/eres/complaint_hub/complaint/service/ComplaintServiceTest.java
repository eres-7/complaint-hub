package pl.eres.complaint_hub.complaint.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.eres.complaint_hub.complaint.entity.Complaint;
import pl.eres.complaint_hub.complaint.exception.ComplaintNotFoundException;
import pl.eres.complaint_hub.complaint.mapper.ComplaintMapper;
import pl.eres.complaint_hub.complaint.mapper.ComplaintMapperImpl;
import pl.eres.complaint_hub.complaint.repository.ComplaintRepository;
import pl.eres.complaint_hub.complaint.util.GeoLocationService;
import pl.eres.generated.model.AddComplaintRequest;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.eres.complaint_hub.complaint.service.ComplaintServiceTest.Fields.*;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private GeoLocationService geoLocationService;

    private final ComplaintMapper complaintMapper = new ComplaintMapperImpl();

    private ComplaintService complaintService;

    private Complaint complaint;

    @BeforeEach
    void setUp() {
        complaintService = new ComplaintService(complaintRepository, geoLocationService, complaintMapper);
        complaint = Complaint.builder()
                .id(COMPLAINT_ID)
                .productId(COMPLAINT_PRODUCT_ID)
                .content(COMPLAINT_CONTENT)
                .reporter(COMPLAINT_REPORTER)
                .country(COMPLAINT_COUNTRY)
                .createdAt(COMPLAINT_CREATED_AT_LDT)
                .count(COMPLAINT_COUNT)
                .build();
    }


    @Test
    void shouldReturnExistingComplaintIdForComplaintAdded() {
        // Given
        var request = new AddComplaintRequest(COMPLAINT_PRODUCT_ID, COMPLAINT_CONTENT, COMPLAINT_REPORTER, COMPLAINT_IP);
        when(complaintRepository.findIdByProductIdAndReporter(request.getProductId(), request.getReporter())).thenReturn(Optional.of(COMPLAINT_ID));
        when(geoLocationService.getCountryByIp(request.getIp())).thenReturn(COMPLAINT_COUNTRY);

        // When
        Long complaintId = complaintService.addComplaint(request);

        // Then
        assertEquals(COMPLAINT_ID, complaintId);
        verify(complaintRepository).upsertComplaint(any(Complaint.class));
    }

    @Test
    void shouldUpdateComplaintContentWhenExists() throws ComplaintNotFoundException {
        // Given
        String newContent = "Updated content";
        when(complaintRepository.existsById(COMPLAINT_ID)).thenReturn(true);

        // When
        complaintService.updateComplaintContent(COMPLAINT_ID, newContent);

        // Then
        verify(complaintRepository).updateComplaintContent(COMPLAINT_ID, newContent);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentComplaint() {
        // Given
        String newContent = "Updated content";
        when(complaintRepository.existsById(COMPLAINT_ID)).thenReturn(false);

        // Expect
        assertThrows(ComplaintNotFoundException.class, () ->
                complaintService.updateComplaintContent(COMPLAINT_ID, newContent));
    }

    @Test
    void shouldReturnComplaintWhenFoundById() {
        // Given
        var expectedComplaint = new pl.eres.generated.model.Complaint(
                COMPLAINT_ID,
                COMPLAINT_PRODUCT_ID,
                COMPLAINT_CONTENT,
                COMPLAINT_CREATED_AT_ODT,
                COMPLAINT_REPORTER,
                COMPLAINT_COUNTRY,
                COMPLAINT_COUNT);
        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(Optional.of(this.complaint));

        // When
        var result = complaintService.getComplaintById(COMPLAINT_ID);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedComplaint, result.get());
    }

    @Test
    void shouldReturnEmptyWhenComplaintNotFoundById() {
        // Given
        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(Optional.empty());

        // When
        var result = complaintService.getComplaintById(COMPLAINT_ID);

        // Then
        assertFalse(result.isPresent());
    }

    static class Fields {
        public static final Long COMPLAINT_ID = 1L;
        public static final long COMPLAINT_PRODUCT_ID = 12345L;
        public static final String COMPLAINT_CONTENT = "Defective product";
        public static final String COMPLAINT_REPORTER = "user1";
        public static final String COMPLAINT_COUNTRY = "USA";
        public static final LocalDateTime COMPLAINT_CREATED_AT_LDT = LocalDateTime.parse("2025-03-03T10:10:10.123");
        public static final OffsetDateTime COMPLAINT_CREATED_AT_ODT = ZonedDateTime.of(COMPLAINT_CREATED_AT_LDT, ZoneId.systemDefault()).toOffsetDateTime();
        public static final int COMPLAINT_COUNT = 1;
        public static final String COMPLAINT_IP = "192.168.0.1";

        private Fields() {

        }

    }

}