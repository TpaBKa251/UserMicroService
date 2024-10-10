package hostel.usermicroservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link hostel.usermicroservice.entity.User}
 */
public record RegisterDto(
        @Size(message = "1 и 50", min = 1, max = 50)
        @NotBlank(message = "Имя дай")
        String firstName,

        @Size(message = "1 и 50", min = 1, max = 50)
        @NotBlank(message = "Фамилию дай")
        String lastName,

        String middleName,

        @Email(message = "Имейл")
        String email,

        String phone,

        String password,

        String roomNumber,

        String groupNumber) {
}