package hostel.usermicroservice.service.impl;

import hostel.usermicroservice.entity.Booking;
import hostel.usermicroservice.enums.BookingStatus;
import hostel.usermicroservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class BookingStateService {

    private final BookingRepository bookingRepository;

    @Scheduled(fixedRate = 60000) // Запускается каждую минуту
    public void updateBookingStatuses() {
        // Обновляем бронирования, которые еще не начались, но должны перейти в состояние "IN_PROGRESS"
        List<Booking> bookingsToStart = bookingRepository.findByStatus(BookingStatus.BOOKED);
        for (Booking booking : bookingsToStart) {
            if (booking.getStartTime().isBefore(LocalDateTime.now())) {
                booking.setStatus(BookingStatus.IN_PROGRESS);
                bookingRepository.save(booking);
            }
        }

        // Обновляем бронирования, которые в процессе и должны быть завершены
        List<Booking> bookingsInProgress = bookingRepository.findByStatus(BookingStatus.IN_PROGRESS);
        for (Booking booking : bookingsInProgress) {
            if (booking.getEndTime().isBefore(LocalDateTime.now())) {
                booking.setStatus(BookingStatus.COMPLETED);
                bookingRepository.save(booking);
            }
        }
    }
}