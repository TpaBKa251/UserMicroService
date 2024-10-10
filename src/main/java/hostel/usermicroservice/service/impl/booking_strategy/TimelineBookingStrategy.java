package hostel.usermicroservice.service.impl.booking_strategy;

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
import hostel.usermicroservice.service.booking_strategy.BookingStrategy;
import hostel.usermicroservice.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class TimelineBookingStrategy implements BookingStrategy {

    private final BookingRepository bookingRepository;
    private final UserServiceImpl userService;

    private LocalTime startBookingTime = LocalTime.of(6, 0);
    private LocalTime endBookingTime = LocalTime.of(0, 0);

    @Override
    public BookingDTO createBooking(BookingRequestDTO bookingRequest, Authentication authentication) {

        User user = userService.getUserByAuthentication(authentication);

        if (
                bookingRequest.startTime().isBefore(LocalDateTime.now())
                || bookingRequest.endTime().isBefore(LocalDateTime.now())
        ) {
            throw new InvalidTimeBookingException("Invalid time booking request");
        }

        if (!bookingRequest.startTime().toLocalDate().equals(bookingRequest.endTime().toLocalDate())) {
            if (!bookingRequest.endTime().minusDays(1).toLocalDate().equals(bookingRequest.startTime().toLocalDate())) {
                throw new InvalidTimeBookingException("Invalid time booking request");
            }

            if (!bookingRequest.endTime().toLocalTime().equals(endBookingTime)) {
                throw new InvalidTimeBookingException("Days of start and end of booking must be the same");
            }
        }

        if (bookingRequest.startTime().getHour() < startBookingTime.getHour()) {
            throw new InvalidTimeBookingException("Booking time must be between 6:00 and 23:59");
        }

        if (
                bookingRequest.startTime().isAfter(bookingRequest.endTime())
                || bookingRequest.startTime().equals(bookingRequest.endTime())
        ) {
            throw new InvalidTimeBookingException("End time must be after start time");
        }



        List<Booking> bookings = bookingRepository.findByStatus(BookingStatus.BOOKED);

        for (Booking booking : bookings) {
            if (bookingRequest.startTime().isBefore(booking.getEndTime())
                    && bookingRequest.endTime().isAfter(booking.getStartTime())) {
                throw new InvalidTimeBookingException(
                        "Your booking crosses another booking: "
                                + BookingMapper.mapToBookingDTO(booking)
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
    public List<AvailableSlotDTO> getAvailableSlots(LocalDate date, Authentication authentication) {

        if (LocalDate.now().plusDays(7).isBefore(date) || date.isBefore(LocalDate.now())) {
            throw new InvalidTimeBookingException("You can only watch slots for the week ahead from today");
        }

        List<Booking> bookings = bookingRepository.findByStatus(BookingStatus.BOOKED);

        if (bookings.isEmpty()) {
            return List.of(
                    new AvailableSlotDTO(
                            null,
                            startBookingTime.atDate(date),
                            endBookingTime.atDate(date.plusDays(1))
                    )
            );
        }

        List<AvailableSlotDTO> availableSlots = new ArrayList<>();

        LocalTime currentStartTime;

        if (!date.equals(LocalDate.now())) {
            currentStartTime = startBookingTime;
        } else {
            currentStartTime = LocalTime.now()
                    .plusHours(1)
                    .withMinute(0)
                    .withSecond(0)
                    .minusNanos(0);
        }


        for (Booking booking : bookings) {
            if (
                    booking.getStartTime().toLocalTime().isAfter(currentStartTime)
                    && booking.getStartTime().toLocalDate().equals(date)
                    && booking.getType().equals(BookingType.TIMELINE)
            ) {

                availableSlots.add(new AvailableSlotDTO(
                                null,
                                LocalDateTime.of(booking.getStartTime().toLocalDate(), currentStartTime),
                                booking.getStartTime()
                ));
            }

            if (booking.getStartTime().toLocalDate().equals(date)) {
                currentStartTime = booking.getEndTime().toLocalTime();
            }
        }

        if (currentStartTime.isAfter(endBookingTime) || currentStartTime.equals(startBookingTime)) {
            availableSlots.add(new AvailableSlotDTO(
                    null,
                    LocalDateTime.of(date, currentStartTime),
                    LocalDateTime.of(date.plusDays(1), endBookingTime)
            ));
        }

        return availableSlots;
    }

    @Override
    public BookingType getSupportedBookingType() {
        return BookingType.TIMELINE;
    }
}
