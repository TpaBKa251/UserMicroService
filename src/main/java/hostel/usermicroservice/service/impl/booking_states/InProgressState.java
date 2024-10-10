package hostel.usermicroservice.service.impl.booking_states;

import hostel.usermicroservice.entity.Booking;
import hostel.usermicroservice.enums.BookingStatus;
import hostel.usermicroservice.repository.BookingRepository;
import hostel.usermicroservice.service.BookingState;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InProgressState implements BookingState {

    @Override
    public void updateStatus(Booking booking, BookingRepository bookingRepository) {
        if (booking.getEndTime().isBefore(LocalDateTime.now())) {
            booking.setStatus(BookingStatus.COMPLETED);
            bookingRepository.save(booking);
        }
    }

    @Override
    public void cancelBooking(Booking booking, BookingRepository bookingRepository) {
        throw new UnsupportedOperationException("Cannot cancel booking while it's in progress");
    }
}
