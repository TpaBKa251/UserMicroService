package hostel.usermicroservice.service;

import hostel.usermicroservice.dto.request.LoginDto;
import hostel.usermicroservice.dto.response.SessionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface SessionService {
    SessionDto createSession(LoginDto loginDto);

    SessionDto getCurrentSession(Authentication authentication);

    List<SessionDto> getAllSessions(Authentication authentication);

    SessionDto logout(Authentication authentication);

    ResponseEntity<?> deleteSession(Authentication authentication, UUID sessionId);
}
