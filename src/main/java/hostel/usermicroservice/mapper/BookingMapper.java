package hostel.usermicroservice.mapper;

import hostel.usermicroservice.dto.response.AvailableSlotDTO;
import hostel.usermicroservice.dto.response.BookingDTO;
import hostel.usermicroservice.entity.Booking;
import hostel.usermicroservice.entity.TimeSlot;

public class BookingMapper {

    public static BookingDTO mapToBookingDTO(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                booking.getType()
        );
    }

    public static AvailableSlotDTO mapToAvailableSlotDTO(TimeSlot timeSlot) {
        return new AvailableSlotDTO(
                timeSlot.getId(),
                timeSlot.getStartTime(),
                timeSlot.getEndTime()
        );
    }

    public static AvailableSlotDTO mapToAvailableSlotDTOFromBooking(Booking booking) {
        return new AvailableSlotDTO(
                null,
                booking.getStartTime(),
                booking.getEndTime()
        );
    }
}
