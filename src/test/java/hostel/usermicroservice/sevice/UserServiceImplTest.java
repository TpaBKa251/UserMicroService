package hostel.usermicroservice.sevice;

import hostel.usermicroservice.dto.request.RegisterDto;
import hostel.usermicroservice.dto.response.UserDto;
import hostel.usermicroservice.dto.response.UserShortDto;
import hostel.usermicroservice.entity.User;
import hostel.usermicroservice.jwt.JwtTokenUtils;
import hostel.usermicroservice.mapper.UserMapper;
import hostel.usermicroservice.repository.UserRepository;
import hostel.usermicroservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тесты класса UserServiceImpl - сервиса объекта User")
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
    }

    @Test
    @DisplayName("Тест регистрации")
    void registerUserTest() {
        RegisterDto registerDto = new RegisterDto(
                "Иван",
                "Иванов",
                "Иванович",
                "ivan.ivanov@mail.com",
                "89991234567",
                "password123",
                "101",
                "IT-21"
        );

        UserDto result = userService.registerUser(registerDto);

        assertThat(result).isNotNull();
        assertThat(result.firstName()).isEqualTo(registerDto.firstName());
        assertThat(result.lastName()).isEqualTo(registerDto.lastName());
        assertThat(result.middleName()).isEqualTo(registerDto.middleName());
        assertThat(result.email()).isEqualTo(registerDto.email());
        assertThat(result.phone()).isEqualTo(registerDto.phone());
        assertThat(result.roomNumber()).isEqualTo(registerDto.roomNumber());
        assertThat(result.groupNumber()).isEqualTo(registerDto.groupNumber());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Успешный поиск пользователя по имени")
    void findUserByUsername_success() {
        String username = "Лапшин Илья Романович";

        when(jwtTokenUtils.getUserIdFromAuthentication(authentication)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByFirstNameAndLastNameAndMiddleName("Лапшин", "Илья", "Романович"))
                .thenReturn(Optional.of(user));

        UserShortDto result = userService.findUserByUsername(username, authentication);

        assertEquals(UserMapper.mapUserInfoToResponse(user), result);
    }

    @Test
    @DisplayName("Тест поиска пользователя по имени, если пользователь с таким именем не найден")
    void findUserByUsername_userNotFound() {
        String username = "Лапшин Илья Романович";

        when(jwtTokenUtils.getUserIdFromAuthentication(authentication)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByFirstNameAndLastNameAndMiddleName("Лапшин", "Илья", "Романович"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.findUserByUsername(username, authentication));
    }

    @Test
    @DisplayName("Тест поиска пользователя по имени, если токен невалиден")
    void findUserByUsername_authenticationNotFound() {
        String username = "Лапшин Илья Романович";

        when(jwtTokenUtils.getUserIdFromAuthentication(authentication)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.findUserByUsername(username, authentication));
    }

    @Test
    @DisplayName("Тест поиска пользователя по имени, если имя неверно задано")
    void findUserByUsername_invalidUsername() {
        String username = "";

        when(jwtTokenUtils.getUserIdFromAuthentication(authentication)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(UsernameNotFoundException.class, () -> userService.findUserByUsername(username, authentication));
    }
}
