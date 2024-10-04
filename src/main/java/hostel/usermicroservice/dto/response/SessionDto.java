package hostel.usermicroservice.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link hostel.usermicroservice.entity.Session}
 */
public record SessionDto(
        UUID id,
        UUID userId,
        String token,
        LocalDateTime createTime,
        LocalDateTime expirationTime,
        boolean active) {
}