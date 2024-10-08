package hostel.usermicroservice.controller;

import hostel.usermicroservice.dto.request.BookingRequestDTO;
import hostel.usermicroservice.dto.response.AvailableSlotDTO;
import hostel.usermicroservice.dto.response.BookingDTO;
import hostel.usermicroservice.enums.BookingType;
import hostel.usermicroservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("hostel/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDTO createBooking(@RequestBody BookingRequestDTO bookingRequest, Authentication authentication) {
        return bookingService.createBooking(bookingRequest, authentication);
    }

    @GetMapping("/{type}/{date}/available-slots")
    public List<AvailableSlotDTO> getAvailableSlots(
            @PathVariable BookingType type,
            @PathVariable LocalDate date,
            Authentication authentication
    ) {
        return bookingService.getAvailableSlots(type, date, authentication);
    }

    @PostMapping("/{bookingId}/cancel")
    public BookingDTO cancelBooking(@PathVariable UUID bookingId, Authentication authentication) {
        return bookingService.cancelBooking(bookingId, authentication);
    }
}
