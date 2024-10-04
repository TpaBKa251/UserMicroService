package hostel.usermicroservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime = LocalDateTime.MAX;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}