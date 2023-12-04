package orci.or.tz.appointments.exceptions;

public class OperationFailedException extends Exception {
    private static final long serialVersionUID = 186345314369642486L;

    public OperationFailedException(String message) {
        super(message);
    }
}
