package hostel.usermicroservice.jwt;

import hostel.usermicroservice.entity.BannedToken;
import hostel.usermicroservice.entity.Session;
import hostel.usermicroservice.entity.User;
import hostel.usermicroservice.repository.BannedTokenRepository;
import hostel.usermicroservice.repository.SessionRepository;
import hostel.usermicroservice.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final BannedTokenRepository bannedTokenRepository;
    private final SessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            boolean isBanned = bannedTokenRepository.findByToken(jwt).isPresent();
            if (isBanned) {
                log.info("Attempt to use a banned token");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is banned");
                return;
            }
            try {
                UUID userId = jwtTokenUtils.getUserIdFromToken(jwt);
                if (userId != null) {
                    User user = userRepository.findById(userId).orElse(null);

                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            user, jwt, Collections.emptyList()
                    );
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            } catch (ExpiredJwtException e) {
                log.debug("Token is expired :(");

                Session session = sessionRepository.findByToken(jwt).orElse(null);
                BannedToken bannedToken = new BannedToken();

                bannedToken.setToken(jwt);
                try {
                    bannedTokenRepository.insertIfNotExist(bannedToken);

                    if (session != null) {
                        if (session.isActive()) {
                            session.setActive(false);
                            sessionRepository.save(session);
                        }
                    }

                } catch (Exception ex) {
                    return;
                }
            } catch (Exception e) {
                log.error("Error processing JWT", e);
            }
        }
        filterChain.doFilter(request, response);
    }
}

