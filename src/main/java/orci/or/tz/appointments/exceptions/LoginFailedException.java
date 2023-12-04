package orci.or.tz.appointments.exceptions;


public class LoginFailedException extends Exception {
    private static final long serialVersionUID = 186345314369642486L;

    public LoginFailedException(String message) {
      super(message);
    } 
}
