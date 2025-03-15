package pl.eres.complaint_hub.complaint.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import pl.eres.complaint_hub.complaint.entity.Complaint;
import pl.eres.complaint_hub.complaint.repository.ComplaintRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("testcontainers")
@SpringBootTest
@AutoConfigureMockMvc
class ComplaintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Test
    void shouldAddComplaint() throws Exception {
        // Given
        long countBefore = complaintRepository.count();

        // When
        mockMvc.perform(post("/api/complaints")
                        .contentType("application/json")
                        .content("{\"productId\": 1, \"content\": \"some content\", \"reporter\": \"user1\", \"ip\": \"127.0.0.1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        // Then
        long countAfter = complaintRepository.count();
        Assertions.assertTrue(countAfter > countBefore);
    }

    @Test
    void shouldIncrementComplaintCountOnly() throws Exception {
        // Given
        Complaint complaint = Complaint.builder().productId(2L).content("some content").createdAt(LocalDateTime.parse("2025-03-03T10:10:10.123")).reporter("user1").country("Unknown").count(1).build();
        complaint = complaintRepository.save(complaint);
        long countBefore = complaintRepository.count();

        // When
        mockMvc.perform(post("/api/complaints")
                        .contentType("application/json")
                        .content("{\"productId\": 2, \"content\": \"some content\", \"reporter\": \"user1\", \"ip\": \"127.0.0.1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(complaint.getId()));

        // Then
        long countAfter = complaintRepository.count();
        Assertions.assertEquals(countAfter, countBefore);
        Optional<Complaint> complaintUpdated = complaintRepository.findById(complaint.getId());
        Assertions.assertTrue(complaintUpdated.isPresent());
        Assertions.assertEquals(2, complaintUpdated.get().getCount());
    }

    @Test
    void shouldUpdateComplaintContent() throws Exception {
        // Given
        Complaint complaint = Complaint.builder().productId(3L).content("some content").createdAt(LocalDateTime.parse("2025-03-03T10:10:10.123")).reporter("user1").country("Unknown").count(1).build();
        complaint = complaintRepository.save(complaint);

        // When
        mockMvc.perform(put("/api/complaints/{id}", complaint.getId())
                        .contentType("application/json")
                        .content("{\"content\": \"New content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(complaint.getId()));

        // Then
        Optional<Complaint> complaintUpdated = complaintRepository.findById(complaint.getId());
        Assertions.assertTrue(complaintUpdated.isPresent());
        Assertions.assertEquals("New content", complaintUpdated.get().getContent());
    }

	@Test
	void shouldReturnNotFoundWhenComplaintNotFoundForUpdate() throws Exception {
		// Given
		Long complaintId = 123L;

		// Expect
		mockMvc.perform(put("/api/complaints/{id}", complaintId)
						.contentType("application/json")
						.content("{\"content\": \"New content\"}"))
				.andExpect(status().isNotFound());
	}

    @Test
    void shouldGetComplaintById() throws Exception {
        // Given
        Complaint complaint = Complaint.builder().productId(4L).content("some content").createdAt(LocalDateTime.parse("2025-03-03T10:10:10.123")).reporter("user1").country("Unknown").count(1).build();
        complaint = complaintRepository.save(complaint);

        // Expect
        mockMvc.perform(get("/api/complaints/{id}", complaint.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(complaint.getId()))
                .andExpect(jsonPath("$.productId").value(4L))
                .andExpect(jsonPath("$.content").value("some content"))
                .andExpect(jsonPath("$.createdAt").value("2025-03-03T10:10:10.123Z"))
                .andExpect(jsonPath("$.reporter").value("user1"))
                .andExpect(jsonPath("$.country").value("Unknown"))
                .andExpect(jsonPath("$.count").value(1L));
    }

	@Test
	void shouldReturnNotFoundWhenComplaintNotFoundForGet() throws Exception {
		// Given
		Long complaintId = 123L;

		// Expect
		mockMvc.perform(get("/api/complaints/{id}", complaintId))
				.andExpect(status().isNotFound());
	}
}
