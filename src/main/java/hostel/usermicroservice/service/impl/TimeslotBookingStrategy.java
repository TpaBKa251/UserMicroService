package hostel.usermicroservice.service.impl;

import hostel.usermicroservice.dto.request.BookingRequestDTO;
import hostel.usermicroservice.dto.response.AvailableSlotDTO;
import hostel.usermicroservice.dto.response.BookingDTO;
import hostel.usermicroservice.entity.Booking;
import hostel.usermicroservice.entity.TimeSlot;
import hostel.usermicroservice.entity.User;
import hostel.usermicroservice.enums.BookingStatus;
import hostel.usermicroservice.enums.BookingType;
import hostel.usermicroservice.exception.SlotAlreadyBookedException;
import hostel.usermicroservice.exception.SlotNotFoundException;
import hostel.usermicroservice.mapper.BookingMapper;
import hostel.usermicroservice.repository.TimeSlotRepository;
import hostel.usermicroservice.service.BookingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class TimeslotBookingStrategy implements BookingStrategy {

    private final TimeSlotRepository timeSlotRepository;
    private final UserServiceImpl userService;

    @Override
    public BookingDTO createBooking(BookingRequestDTO bookingRequest, Authentication authentication) {

        User user = userService.getUserByAuthentication(authentication);

        // Логика создания бронирования для таймслотов
        TimeSlot slot = timeSlotRepository.findById(bookingRequest.slotId())
                .orElseThrow(() -> new SlotNotFoundException("Time slot not found"));
        if (slot.isBooked()) {
            throw new SlotAlreadyBookedException("Slot already booked");
        }
        slot.setBooked(true);
        Booking booking = new Booking();
        booking.setTimeSlot(slot);
        booking.setStatus(BookingStatus.BOOKED);
        booking.setUser(user);
        booking.setType(BookingType.TIME_SLOT);

        return BookingMapper.mapToBookingDTO(booking);
    }

    @Override
    public List<AvailableSlotDTO> getAvailableSlots(Authentication authentication) {
        // Возвращаем список доступных слотов для бронирования
        return timeSlotRepository.findAllByBooked(false)
                .stream().map(slot -> new AvailableSlotDTO(slot.getId(), slot.getStartTime(), slot.getEndTime()))
                .collect(Collectors.toList());
    }
}
