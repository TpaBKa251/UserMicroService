package hostel.usermicroservice.service;

import hostel.usermicroservice.dto.request.BookingRequestDTO;
import hostel.usermicroservice.dto.response.AvailableSlotDTO;
import hostel.usermicroservice.dto.response.BookingDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BookingStrategy {

    BookingDTO createBooking(BookingRequestDTO bookingRequest, Authentication authentication);

    List<AvailableSlotDTO> getAvailableSlots(Authentication authentication);
}
