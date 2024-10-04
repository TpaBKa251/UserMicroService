package hostel.usermicroservice.mapper;

import hostel.usermicroservice.dto.response.SessionDto;
import hostel.usermicroservice.entity.Session;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {
    public static SessionDto mapSessionResponse(Session session) {
        return new SessionDto(
                session.getId(),
                session.getUserId(),
                session.getToken(),
                session.getCreateTime(),
                session.getExpirationTime(),
                session.isActive()
        );
    }
}
