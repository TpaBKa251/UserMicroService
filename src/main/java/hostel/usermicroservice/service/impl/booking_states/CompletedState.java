package hostel.usermicroservice.service.impl.booking_states;

import hostel.usermicroservice.entity.Booking;
import hostel.usermicroservice.repository.BookingRepository;
import hostel.usermicroservice.service.BookingState;
import org.springframework.stereotype.Service;

@Service
public class CompletedState implements BookingState {

    @Override
    public void updateStatus(Booking booking, BookingRepository bookingRepository) {
    }

    @Override
    public void cancelBooking(Booking booking, BookingRepository bookingRepository) {
        throw new UnsupportedOperationException("Cannot cancel completed booking");
    }
}
