package hostel.usermicroservice.service.impl;

import hostel.usermicroservice.dto.request.BookingRequestDTO;
import hostel.usermicroservice.dto.response.AvailableSlotDTO;
import hostel.usermicroservice.dto.response.BookingDTO;
import hostel.usermicroservice.entity.Booking;
import hostel.usermicroservice.enums.BookingStatus;
import hostel.usermicroservice.enums.BookingType;
import hostel.usermicroservice.exception.BookingNotFoundException;
import hostel.usermicroservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final TimelineBookingStrategy timelineStrategy;
    private final TimeslotBookingStrategy timeslotStrategy;
    private final BookingRepository bookingRepository;

    public BookingDTO createBooking(BookingRequestDTO bookingRequest, Authentication authentication) {
        if (bookingRequest.type() == BookingType.TIMELINE) {
            return timelineStrategy.createBooking(bookingRequest, authentication);
        } else if (bookingRequest.type() == BookingType.TIME_SLOT) {
            return timeslotStrategy.createBooking(bookingRequest, authentication);
        }
        throw new IllegalArgumentException("Unknown booking type");
    }

    public List<AvailableSlotDTO> getAvailableSlots(BookingType type, Authentication authentication) {
        if (type == BookingType.TIMELINE) {
            return timelineStrategy.getAvailableSlots(authentication);
        } else if (type == BookingType.TIME_SLOT) {
            return timeslotStrategy.getAvailableSlots(authentication);
        }
        throw new IllegalArgumentException("Unknown booking type");
    }

    public boolean cancelBooking(UUID bookingId, Authentication authentication) {
        authentication.getCredentials();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.BOOKED || booking.getStatus() == BookingStatus.IN_PROGRESS) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            return true;
        }
        return false;
    }
}