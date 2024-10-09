package hostel.usermicroservice.dto.response;

import hostel.usermicroservice.enums.BookingStatus;
import hostel.usermicroservice.enums.BookingType;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingDTO(
        UUID id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BookingStatus status,
        BookingType type
) {
}
