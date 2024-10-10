package hostel.usermicroservice.repository;

import hostel.usermicroservice.entity.Booking;
import hostel.usermicroservice.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByStatusAndStartTime(BookingStatus status, LocalDateTime startTime);
}
