package pl.eres.complaint_hub.complaint.mapper;

import org.mapstruct.Mapper;
import pl.eres.generated.model.Complaint;

import java.time.*;

@Mapper(componentModel = "spring")
public interface ComplaintMapper {
    Complaint map(pl.eres.complaint_hub.complaint.entity.Complaint entity);

    default OffsetDateTime mapLocalDateTimeToOffsetDateTime(LocalDateTime localDateTime) {
        return (localDateTime != null) ? localDateTime.atOffset(ZoneOffset.UTC) : null;
    }
}
