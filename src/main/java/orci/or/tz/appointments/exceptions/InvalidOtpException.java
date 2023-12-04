package orci.or.tz.appointments.exceptions;

public class InvalidOtpException extends Exception {
    private static final long serialVersionUID = 186345314369642486L;

    public InvalidOtpException(String message) {
      super(message);
    }
}
