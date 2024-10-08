package hostel.usermicroservice.controller;

import hostel.usermicroservice.dto.request.LoginDto;
import hostel.usermicroservice.dto.response.SessionDto;
import hostel.usermicroservice.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("hostel/sessions")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    public SessionDto login(@RequestBody @Valid LoginDto loginDto) {
        return sessionService.createSession(loginDto);
    }

    @GetMapping("/current")
    public SessionDto getCurrentSession(Authentication authentication) {
        return sessionService.getCurrentSession(authentication);
    }

    @GetMapping("/all")
    public List<SessionDto> getAllSessions(Authentication authentication) {
        return sessionService.getAllSessions(authentication);
    }

    @PatchMapping("/logout")
    public SessionDto logout(Authentication authentication) {
        return sessionService.logout(authentication);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSession(Authentication authentication, @PathVariable UUID id) {
        return sessionService.deleteSession(authentication, id);
    }
}
