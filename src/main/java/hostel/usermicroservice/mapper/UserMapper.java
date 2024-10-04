package hostel.usermicroservice.mapper;

import hostel.usermicroservice.dto.request.RegisterDto;
import hostel.usermicroservice.dto.response.UserDto;
import hostel.usermicroservice.dto.response.UserShortDto;
import hostel.usermicroservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User mapRegisterBodyToUser(RegisterDto body) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(body.password()));
        user.setFirstName(body.firstName());
        user.setLastName(body.lastName());
        user.setMiddleName(body.middleName());
        user.setEmail(body.email());
        user.setPhone(body.phone());
        user.setGroupNumber(body.groupNumber());
        user.setRoomNumber(body.roomNumber());
        return user;
    }

    public static UserDto mapUserToResponse(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getEmail(),
                user.getPhone(),
                user.getRoomNumber(),
                user.getGroupNumber(),
                user.getRegisterDateTime(),
                user.getUpdateDateTime()
        );
    }

    public static UserShortDto mapUserInfoToResponse(User user) {
        return new UserShortDto(
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getEmail(),
                user.getPhone(),
                user.getRoomNumber(),
                user.getGroupNumber()
        );
    }
}
