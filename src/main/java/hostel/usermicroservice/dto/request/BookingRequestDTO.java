package hostel.usermicroservice.dto.request;

import hostel.usermicroservice.enums.BookingType;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingRequestDTO(
        BookingType type,
        LocalDateTime startTime,
        LocalDateTime endTime,
        UUID slotId
) {
}
