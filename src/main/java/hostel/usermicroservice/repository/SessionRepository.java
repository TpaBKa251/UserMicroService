package hostel.usermicroservice.repository;

import hostel.usermicroservice.entity.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    List<Session> findAllByUserId(UUID userId);

    Optional<Session> findByToken(String token);
}