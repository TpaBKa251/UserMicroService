package hostel.usermicroservice.entity;

import hostel.usermicroservice.enums.BookingStatus;
import hostel.usermicroservice.enums.BookingType;
import hostel.usermicroservice.repository.BookingRepository;
import hostel.usermicroservice.service.BookingState;
import hostel.usermicroservice.service.impl.booking_states.BookedState;
import hostel.usermicroservice.service.impl.booking_states.CancelState;
import hostel.usermicroservice.service.impl.booking_states.CompletedState;
import hostel.usermicroservice.service.impl.booking_states.InProgressState;
import hostel.usermicroservice.service.impl.booking_states.NotBookedState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "booking")
@Entity
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(nullable = false, name = "start_time")
    private LocalDateTime startTime;

    @Column(nullable = false, name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Transient
    private BookingState bookingState;

    @Enumerated(EnumType.STRING)
    private BookingType type;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "time_slot_id")
    private TimeSlot timeSlot;

    @PostLoad
    public void initializeBookingState() {
        switch (this.status) {
            case CANCELLED -> this.bookingState = new CancelState();
            case NOT_BOOKED -> this.bookingState = new NotBookedState();
            case BOOKED -> this.bookingState = new BookedState();
            case IN_PROGRESS -> this.bookingState = new InProgressState();
            case COMPLETED -> this.bookingState = new CompletedState();
            default -> throw new IllegalStateException("Unknown booking status: " + this.status);
        }
    }

}
