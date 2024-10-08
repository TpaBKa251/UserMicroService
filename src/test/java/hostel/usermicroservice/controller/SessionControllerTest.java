package hostel.usermicroservice.controller;

import hostel.usermicroservice.entity.User;
import hostel.usermicroservice.repository.SessionRepository;
import hostel.usermicroservice.repository.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Интеграционные тесты контроллера сессии SessionController")
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        sessionRepository.deleteAll();

        User user = Instancio.create(User.class);
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(user);
    }

    @Test
    @DisplayName("Тест успешного входа в аккаунт")
    void login_success() throws Exception {
        mockMvc.perform(post("/hostel/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "test@example.com",
                                    "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.expirationTime").isNotEmpty());

        assertThat(sessionRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Тест провального входа в аккаунт")
    void login_invalidCredentials() throws Exception {
        mockMvc.perform(post("/hostel/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "wrong@example.com",
                                    "password": "wrongPassword"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Incorrect email or password"));

        assertThat(sessionRepository.findAll()).isEmpty();
    }
}
