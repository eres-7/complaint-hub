package pl.eres.complaint_hub.complaint.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.eres.complaint_hub.complaint.exception.ComplaintNotFoundException;
import pl.eres.complaint_hub.complaint.service.ComplaintService;
import pl.eres.generated.api.ApiApi;
import pl.eres.generated.model.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ComplaintController implements ApiApi {

    private final ComplaintService complaintService;

    @Override
    public ResponseEntity<AddComplaintResponse> addComplaint(@Valid @RequestBody AddComplaintRequest request) {
        log.info("Received a request to add a complaint for product ID: {} from user: {}", request.getProductId(), request.getReporter());
        Long complaintId = complaintService.addComplaint(request);
        log.info("Complaint recorded for product ID: {} from user: {}", request.getProductId(), request.getReporter());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AddComplaintResponse(complaintId));
    }

    @Override
    public ResponseEntity<UpdateComplaintResponse> updateComplaintContent(Long id, @Valid @RequestBody UpdateComplaintRequest request) {
        log.info("Received a request to change the complaint content for ID: {}", id);
        try {
            complaintService.updateComplaintContent(id, request.getContent());
        } catch (ComplaintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("The complaint content has been updated for ID: {}", id);
        return ResponseEntity.ok(new UpdateComplaintResponse(id));
    }

    @Override
    public ResponseEntity<Complaint> getComplaintById(Long id) {
        log.info("Received a request to retrieve complaint data for ID: {}", id);
        Optional<Complaint> complaint = complaintService.getComplaintById(id);
        if (complaint.isPresent()) {
            log.info("Returned complaint data for ID: {}", id);
            return ResponseEntity.ok(complaint.get());
        } else {
            log.warn("Complaint not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}