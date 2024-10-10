package hostel.usermicroservice.service;

import hostel.usermicroservice.entity.Booking;
import hostel.usermicroservice.repository.BookingRepository;

public interface BookingState {

    void updateStatus(Booking booking, BookingRepository bookingRepository);

    void cancelBooking(Booking booking, BookingRepository bookingRepository);
}
