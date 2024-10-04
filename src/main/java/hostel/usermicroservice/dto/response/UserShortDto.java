package hostel.usermicroservice.dto.response;


import lombok.Builder;

/**
 * DTO for {@link hostel.usermicroservice.entity.User}
 */
@Builder
public record UserShortDto(String firstName, String lastName, String middleName, String email, String phone,
                           String roomNumber, String groupNumber) {
}