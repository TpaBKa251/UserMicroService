package hostel.usermicroservice.service;

import hostel.usermicroservice.dto.request.BookingRequestDTO;
import hostel.usermicroservice.dto.response.AvailableSlotDTO;
import hostel.usermicroservice.dto.response.BookingDTO;
import hostel.usermicroservice.enums.BookingType;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingService {

    BookingDTO createBooking(BookingRequestDTO bookingRequest, Authentication authentication);

    List<AvailableSlotDTO> getAvailableSlots(BookingType type, LocalDate date, Authentication authentication);

    BookingDTO cancelBooking(UUID bookingId, Authentication authentication);
}
