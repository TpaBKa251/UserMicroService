package hostel.usermicroservice.controller;

import hostel.usermicroservice.dto.request.BookingRequestDTO;
import hostel.usermicroservice.dto.response.AvailableSlotDTO;
import hostel.usermicroservice.dto.response.BookingDTO;
import hostel.usermicroservice.enums.BookingType;
import hostel.usermicroservice.service.impl.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("hostel/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDTO createBooking(@RequestBody BookingRequestDTO bookingRequest, Authentication authentication) {
        return null;
    }

    @GetMapping("/{type}/available-slots")
    public List<AvailableSlotDTO> getAvailableSlots(@PathVariable BookingType type, Authentication authentication) {
        return null;
    }

    @PostMapping("/{bookingId}/cancel")
    public BookingDTO cancelBooking(@PathVariable UUID bookingId, Authentication authentication) {
        return null;
    }
}
