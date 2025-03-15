package pl.eres.complaint_hub.complaint.exception;

public class ComplaintNotFoundException extends Exception {

    public ComplaintNotFoundException(Long complaintId) {
        super("Complaint with ID " + complaintId + " not found");
    }

    public ComplaintNotFoundException(Long complaintId, Throwable cause) {
        super("Complaint with ID " + complaintId + " not found", cause);
    }
}
