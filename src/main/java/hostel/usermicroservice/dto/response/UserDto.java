package hostel.usermicroservice.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link hostel.usermicroservice.entity.User}
 */
@Builder
public record UserDto(UUID id, String firstName, String lastName, String middleName, String email, String phone,
                      String roomNumber, String groupNumber, LocalDateTime registerDateTime,
                      LocalDateTime updateDateTime) {
}