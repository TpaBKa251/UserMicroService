package hostel.usermicroservice.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record AvailableSlotDTO(
        UUID id,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
