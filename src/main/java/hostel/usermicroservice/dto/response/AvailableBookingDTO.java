package hostel.usermicroservice.dto.response;

import java.time.LocalDateTime;

public record AvailableBookingDTO(
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
