package hostel.usermicroservice.repository;

import hostel.usermicroservice.entity.BannedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BannedTokenRepository extends JpaRepository<BannedToken, String> {
    Optional<BannedToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO banned_token (token) VALUES (:#{#bannedToken.token}) ON CONFLICT (token) DO NOTHING", nativeQuery = true)
    void insertIfNotExist(@Param("bannedToken") BannedToken bannedToken);
}