package pl.eres.complaint_hub.complaint.mapper;

import org.mapstruct.Mapper;
import pl.eres.generated.model.Complaint;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public interface ComplaintMapper {
    Complaint map(pl.eres.complaint_hub.complaint.entity.Complaint entity);

    default OffsetDateTime mapLocalDateTimeToOffsetDateTime(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        return localDateTime != null ? ZonedDateTime.of(localDateTime, zoneId).toOffsetDateTime() : null;
    }
}
