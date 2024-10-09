package hostel.usermicroservice.exception;

public class InvalidTimeBookingException extends RuntimeException {
    public InvalidTimeBookingException(String message) {
        super(message);
    }
}
