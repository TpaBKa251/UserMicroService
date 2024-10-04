package hostel.usermicroservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "banned_token")
public class BannedToken {
    @Id
    @Column(name = "token", nullable = false)
    private String token;

}