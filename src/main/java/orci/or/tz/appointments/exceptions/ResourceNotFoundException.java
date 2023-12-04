package orci.or.tz.appointments.exceptions;

public class ResourceNotFoundException extends Exception {
    private static final long serialVersionUID = 186345314369642486L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}