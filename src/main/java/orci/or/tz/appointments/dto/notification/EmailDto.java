package orci.or.tz.appointments.dto.notification;

import lombok.Data;

@Data
public class EmailDto {
    
    private String to;
    private String subject;
    private String body;
}
