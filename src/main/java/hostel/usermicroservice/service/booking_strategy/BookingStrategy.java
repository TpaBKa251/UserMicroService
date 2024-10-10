package hostel.usermicroservice.service.booking_strategy;

import hostel.usermicroservice.dto.request.BookingRequestDTO;
import hostel.usermicroservice.dto.response.AvailableSlotDTO;
import hostel.usermicroservice.dto.response.BookingDTO;
import hostel.usermicroservice.enums.BookingType;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

public interface BookingStrategy {

    BookingDTO createBooking(BookingRequestDTO bookingRequest, Authentication authentication);

    List<AvailableSlotDTO> getAvailableSlots(LocalDate date, Authentication authentication);

    BookingType getSupportedBookingType();
}
