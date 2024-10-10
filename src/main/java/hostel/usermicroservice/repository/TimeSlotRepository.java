package hostel.usermicroservice.repository;

import hostel.usermicroservice.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, UUID> {

    List<TimeSlot> findAllByIsBooked(boolean isBooked);

    @Query("select t from TimeSlot t order by t.startTime desc limit 1")
    TimeSlot findLastSlot();
}
