package hostel.usermicroservice.service.impl;

import hostel.usermicroservice.dto.request.BookingRequestDTO;
import hostel.usermicroservice.dto.response.AvailableSlotDTO;
import hostel.usermicroservice.dto.response.BookingDTO;
import hostel.usermicroservice.entity.Booking;
import hostel.usermicroservice.enums.BookingType;
import hostel.usermicroservice.exception.BookingNotFoundException;
import hostel.usermicroservice.mapper.BookingMapper;
import hostel.usermicroservice.repository.BookingRepository;
import hostel.usermicroservice.service.BookingService;
import hostel.usermicroservice.service.booking_strategy.BookingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {
    private final EnumMap<BookingType, BookingStrategy> bookingStrategyMap = new EnumMap<>(BookingType.class);
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(List<BookingStrategy> strategies, BookingRepository bookingRepository) {
        for (BookingStrategy strategy : strategies) {
            bookingStrategyMap.put(strategy.getSupportedBookingType(), strategy);
        }
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingDTO createBooking(BookingRequestDTO bookingRequest, Authentication authentication) {
        BookingStrategy strategy = bookingStrategyMap.get(bookingRequest.type());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown booking type");
        }
        return strategy.createBooking(bookingRequest, authentication);
    }

    @Override
    public List<AvailableSlotDTO> getAvailableSlots(BookingType type, LocalDate date, Authentication authentication) {
        BookingStrategy strategy = bookingStrategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown booking type");
        }
        return strategy.getAvailableSlots(date, authentication);
    }

    @Override
    public BookingDTO cancelBooking(UUID bookingId, Authentication authentication) {
        authentication.getCredentials();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        booking.getBookingState().cancelBooking(booking, bookingRepository);

        return BookingMapper.mapToBookingDTO(booking);
    }
}