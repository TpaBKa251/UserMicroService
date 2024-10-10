package hostel.usermicroservice.service.impl;

import hostel.usermicroservice.dto.request.LoginDto;
import hostel.usermicroservice.dto.response.SessionDto;
import hostel.usermicroservice.entity.BannedToken;
import hostel.usermicroservice.entity.Session;
import hostel.usermicroservice.entity.User;
import hostel.usermicroservice.exception.AccessRightsException;
import hostel.usermicroservice.exception.SessionNotFoundException;
import hostel.usermicroservice.jwt.JwtTokenUtils;
import hostel.usermicroservice.mapper.SessionMapper;
import hostel.usermicroservice.repository.BannedTokenRepository;
import hostel.usermicroservice.repository.SessionRepository;
import hostel.usermicroservice.repository.UserRepository;
import hostel.usermicroservice.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserServiceImpl userService;
    private final SessionRepository sessionRepository;
    private final BannedTokenRepository bannedTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SessionDto createSession(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.email())
                .filter(u -> passwordEncoder.matches(loginDto.password(), u.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Incorrect email or password"));

        String token = jwtTokenUtils.generateToken(user);
        LocalDateTime expiredDate = jwtTokenUtils.getExpirationDateFromToken(token).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Session session = new Session();
        session.setUserId(user.getId());
        session.setToken(token);
        session.setExpirationTime(expiredDate);
        sessionRepository.save(session);

        return SessionMapper.mapSessionResponse(session);
    }

    @Override
    public SessionDto getCurrentSession(Authentication authentication) {
        String currentToken = (String) authentication.getCredentials();

        Optional<Session> sessionOptional = sessionRepository.findByToken(currentToken);
        if (sessionOptional.isPresent()) {
            Session session = sessionOptional.get();
            return SessionMapper.mapSessionResponse(
                    session);
        } else {
            throw new SessionNotFoundException("Session not found");
        }
    }

    @Override
    public List<SessionDto> getAllSessions(Authentication authentication) {
        User user = userService.getUserByAuthentication(authentication);

        List<Session> sessions = sessionRepository.findAllByUserId(user.getId());
        return sessions.stream()
                .map(SessionMapper::mapSessionResponse)
                .toList();
    }

    @Override
    public SessionDto logout(Authentication authentication) {
        String currentToken = (String) authentication.getCredentials();

        Session session = sessionRepository.findByToken(currentToken).orElse(null);

        BannedToken bannedToken = new BannedToken();
        bannedToken.setToken(authentication.getCredentials().toString());
        bannedTokenRepository.save(bannedToken);

        assert session != null;

        session.setActive(false);
        sessionRepository.save(session);

        return SessionMapper.mapSessionResponse(session);
    }

    @Override
    public ResponseEntity<?> deleteSession(Authentication authentication, UUID sessionId) {
        UUID currentUserId = userService.getUserByAuthentication(authentication).getId();

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        if (!session.getUserId().equals(currentUserId)) {
            throw new AccessRightsException("You can only delete your own sessions");
        }

        BannedToken bannedToken = new BannedToken();
        bannedToken.setToken(authentication.getCredentials().toString());

        bannedTokenRepository.save(bannedToken);
        sessionRepository.deleteById(sessionId);

        return ResponseEntity.ok().build();
    }
}
