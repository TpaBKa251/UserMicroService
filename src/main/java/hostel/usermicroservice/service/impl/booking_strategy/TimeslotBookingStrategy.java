package hostel.usermicroservice.service.impl.booking_strategy;

import hostel.usermicroservice.dto.request.BookingRequestDTO;
import hostel.usermicroservice.dto.response.AvailableSlotDTO;
import hostel.usermicroservice.dto.response.BookingDTO;
import hostel.usermicroservice.entity.Booking;
import hostel.usermicroservice.entity.TimeSlot;
import hostel.usermicroservice.entity.User;
import hostel.usermicroservice.enums.BookingStatus;
import hostel.usermicroservice.enums.BookingType;
import hostel.usermicroservice.exception.InvalidTimeBookingException;
import hostel.usermicroservice.exception.SlotAlreadyBookedException;
import hostel.usermicroservice.exception.SlotNotFoundException;
import hostel.usermicroservice.mapper.BookingMapper;
import hostel.usermicroservice.repository.BookingRepository;
import hostel.usermicroservice.repository.TimeSlotRepository;
import hostel.usermicroservice.service.booking_strategy.BookingStrategy;
import hostel.usermicroservice.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class TimeslotBookingStrategy implements BookingStrategy {

    private final TimeSlotRepository timeSlotRepository;
    private final BookingRepository bookingRepository;
    private final UserServiceImpl userService;

    @Override
    public BookingDTO createBooking(BookingRequestDTO bookingRequest, Authentication authentication) {

        User user = userService.getUserByAuthentication(authentication);

        TimeSlot slot = timeSlotRepository.findById(bookingRequest.slotId())
                .orElseThrow(() -> new SlotNotFoundException("Time slot not found"));

        if (slot.isBooked()) {
            throw new SlotAlreadyBookedException("Slot already booked");
        }

        if (slot.getStartTime().isBefore(LocalDateTime.now())) {
            throw new InvalidTimeBookingException("Invalid time slot");
        }

        slot.setBooked(true);
        slot.setUser(user);

        timeSlotRepository.save(slot);

        Booking booking = new Booking();
        booking.setTimeSlot(slot);
        booking.setUser(user);
        booking.setType(BookingType.TIME_SLOT);
        booking.setStatus(BookingStatus.BOOKED);
        booking.setStartTime(slot.getStartTime());
        booking.setEndTime(slot.getEndTime());

        bookingRepository.save(booking);

        return BookingMapper.mapToBookingDTO(booking);
    }

    @Override
    public List<AvailableSlotDTO> getAvailableSlots(LocalDate date, Authentication authentication) {

        if (LocalDate.now().plusDays(7).isBefore(date) || date.isBefore(LocalDate.now())) {
            throw new InvalidTimeBookingException("You can only watch slots for the week ahead from today");
        }

        List<TimeSlot> slots = timeSlotRepository.findAllByIsBooked(false);

        List<AvailableSlotDTO> availableSlots = new ArrayList<>();

        for (TimeSlot slot : slots) {
            if (
                    slot.getStartTime().toLocalDate().equals(date)
                    && slot.getStartTime().isAfter(LocalDateTime.now())
            ) {
                availableSlots.add(new AvailableSlotDTO(
                        slot.getId(),
                        slot.getStartTime(),
                        slot.getEndTime()
                ));
            }
        }

        return availableSlots;
    }

    @Override
    public BookingType getSupportedBookingType() {
        return BookingType.TIME_SLOT;
    }
}
