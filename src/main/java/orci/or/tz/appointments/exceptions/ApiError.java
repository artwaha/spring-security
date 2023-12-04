package orci.or.tz.appointments.exceptions;


import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApiError {

    private HttpStatus status;
    private String message;
    private HashMap<String,List<String>> errors;

    public ApiError() {
        super();
        status = HttpStatus.BAD_REQUEST;
        message = "Invalid Request";
        errors = new HashMap<String,List<String>>();
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HashMap<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, List<String>> errors) {
        this.errors = errors;
    }

    //TODO: Do error checking
    public void addError(String key, String value) {
        if (errors.containsKey(key)) {
            errors.get(key).add(value);
            return;
        }
        List<String> list = new ArrayList<String>();
        list.add(value);
        errors.put(key, list);
    }

}
