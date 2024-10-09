package hostel.usermicroservice.service.impl;

import hostel.usermicroservice.dto.request.BookingRequestDTO;
import hostel.usermicroservice.dto.response.AvailableSlotDTO;
import hostel.usermicroservice.dto.response.BookingDTO;
import hostel.usermicroservice.entity.Booking;
import hostel.usermicroservice.entity.User;
import hostel.usermicroservice.enums.BookingStatus;
import hostel.usermicroservice.enums.BookingType;
import hostel.usermicroservice.exception.InvalidTimeBookingException;
import hostel.usermicroservice.mapper.BookingMapper;
import hostel.usermicroservice.repository.BookingRepository;
import hostel.usermicroservice.repository.UserRepository;
import hostel.usermicroservice.service.BookingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class TimelineBookingStrategy implements BookingStrategy {

    private final BookingRepository bookingRepository;
    private final UserServiceImpl userService;

    private LocalTime startBookingTime = LocalTime.of(6, 0);
    private LocalTime endBookingTime = LocalTime.of(23, 59);

    @Override
    public BookingDTO createBooking(BookingRequestDTO bookingRequest, Authentication authentication) {

        User user = userService.getUserByAuthentication(authentication);

        if (bookingRequest.startTime().getDayOfWeek() != bookingRequest.endTime().getDayOfWeek()) {
            throw new InvalidTimeBookingException("Days of start and end of booking must be the same");
        }

        if (bookingRequest.startTime().getHour() < startBookingTime.getHour()
                || bookingRequest.endTime().getHour() > endBookingTime.getHour()) {
            throw new InvalidTimeBookingException("Booking time must be between 6:00 and 23:59");
        }

        if (bookingRequest.startTime().isBefore(bookingRequest.endTime())) {
            throw new InvalidTimeBookingException("End time must be after start time");
        }

        List<Booking> bookings = bookingRepository.findByStatus(BookingStatus.BOOKED);

        for (Booking booking : bookings) {
            if (
                    (bookingRequest.startTime().isBefore(booking.getStartTime())
                            && bookingRequest.endTime().isAfter(booking.getStartTime()))
                            || (bookingRequest.startTime().isAfter(booking.getStartTime())
                            && bookingRequest.endTime().isBefore(booking.getEndTime()))
                            || (bookingRequest.startTime().isBefore(booking.getStartTime())
                            && bookingRequest.startTime().isAfter(booking.getEndTime()))
            ) {
                throw new InvalidTimeBookingException(
                        "Your booking crosses another booking: " + BookingMapper.mapToBookingDTO(booking)
                );
            }
        }

        // Логика создания бронирования для таймлайна
        Booking booking = new Booking();
        booking.setStartTime(bookingRequest.startTime().withMinute(0).withSecond(0).minusNanos(0));
        booking.setEndTime(bookingRequest.endTime().withMinute(0).withSecond(0).minusNanos(0));
        booking.setStatus(BookingStatus.BOOKED);
        booking.setType(BookingType.TIMELINE);
        booking.setUser(user);

        bookingRepository.save(booking);

        return BookingMapper.mapToBookingDTO(booking);
    }

    @Override
    public List<AvailableSlotDTO> getAvailableSlots(Authentication authentication) {
        List<Booking> bookings = bookingRepository.findByStatus(BookingStatus.BOOKED);

        if (bookings.isEmpty()) {
            return List.of(
                    new AvailableSlotDTO(
                            null,
                            startBookingTime.atDate(LocalDate.now()),
                            endBookingTime.atDate(LocalDate.now())
                    )
            );
        }

        List<AvailableSlotDTO> availableSlots = new ArrayList<>();

        LocalTime currentStarTime = startBookingTime;

        for (Booking booking : bookings) {
            if (booking.getStartTime().toLocalTime().isAfter(currentStarTime)) {
                availableSlots.add(new AvailableSlotDTO(
                                null,
                                LocalDateTime.of(booking.getStartTime().toLocalDate(), currentStarTime),
                                booking.getStartTime()
                ));

                currentStarTime = booking.getEndTime().toLocalTime();
            }
        }

        if (currentStarTime.isBefore(endBookingTime)) {
            availableSlots.add(new AvailableSlotDTO(
                    null,
                    LocalDateTime.of(bookings.get(0).getStartTime().toLocalDate(), currentStarTime),
                    LocalDateTime.of(bookings.get(0).getStartTime().toLocalDate(), endBookingTime)
            ));
        }

        return availableSlots;
    }

    public void editBookingTime(LocalTime startBookingTime, LocalTime endBookingTime) {
        this.startBookingTime = startBookingTime;
        this.endBookingTime = endBookingTime;
    }
}
