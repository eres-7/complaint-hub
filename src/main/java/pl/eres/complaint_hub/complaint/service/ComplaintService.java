package pl.eres.complaint_hub.complaint.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.eres.complaint_hub.complaint.entity.Complaint;
import pl.eres.complaint_hub.complaint.exception.ComplaintNotFoundException;
import pl.eres.complaint_hub.complaint.mapper.ComplaintMapper;
import pl.eres.complaint_hub.complaint.repository.ComplaintRepository;
import pl.eres.complaint_hub.complaint.util.GeoLocationService;
import pl.eres.generated.model.AddComplaintRequest;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final GeoLocationService geoLocationService;
    private final ComplaintMapper complaintMapper;

    @Transactional
    public Long addComplaint(AddComplaintRequest request) {
        Complaint newComplaint = prepareNewComplaint(request);
        complaintRepository.upsertComplaint(newComplaint);
        Long complaintId = complaintRepository.findIdByProductIdAndReporter(request.getProductId(), request.getReporter()).get();
        log.debug("Complaint for product ID: {} from user: {} has been added, ID: {}", request.getProductId(), request.getReporter(), complaintId);
        return complaintId;
    }

    private Complaint prepareNewComplaint(AddComplaintRequest request) {
        String country = geoLocationService.getCountryByIp(request.getIp());
        log.debug("Country name returned from the service for IP: {} is: {}", request.getIp(), country);
        return Complaint.builder()
                .productId(request.getProductId())
                .content(request.getContent())
                .reporter(request.getReporter())
                .country(country)
                .createdAt(LocalDateTime.now())
                .count(1)
                .build();
    }

    @Transactional
    public void updateComplaintContent(Long id, String newContent) throws ComplaintNotFoundException {
        var complaintExists = complaintRepository.existsById(id);
        log.debug("Complaint with ID: {} exists and will be updated", id);
        if (complaintExists) {
            complaintRepository.updateComplaintContent(id, newContent);
            log.debug("The content of the complaint with ID: {} has been updated", id);
        } else {
            log.debug("Complaint with ID: {} not found", id);
            throw new ComplaintNotFoundException(id);
        }
    }

    public Optional<pl.eres.generated.model.Complaint> getComplaintById(Long id) {
        return complaintRepository.findById(id).map(complaintMapper::map);
    }
}