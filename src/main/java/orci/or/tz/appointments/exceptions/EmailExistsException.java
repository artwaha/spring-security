package orci.or.tz.appointments.exceptions;

public class EmailExistsException extends Exception {
    private static final long serialVersionUID = 186345314369642486L;

    public EmailExistsException(String message) {
        super(message);
    }
}
