package hostel.usermicroservice.service.impl;

import hostel.usermicroservice.entity.TimeSlot;
import hostel.usermicroservice.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@EnableScheduling
@RequiredArgsConstructor
@Service
public class BookingSchedulingService {

    private final TimeSlotRepository timeSlotRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Tomsk")
    public void bookTimeSlots() {
        LocalDateTime startTime =
                LocalDateTime
                .now()
                .withSecond(0)
                .withSecond(0)
                .plusDays(7)
                .plusHours(20);

        for (int i = 0; i < 24; i++) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(startTime);
            slot.setEndTime(startTime.plusMinutes(10));
            startTime = startTime.plusMinutes(10);
            timeSlotRepository.save(slot);
        }
    }
}
