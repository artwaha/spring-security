package orci.or.tz.appointments.dto.booking;

import lombok.Data;

@Data
public class BookCountDto {
    private Integer total;
    private Integer pending;
    private Integer upcoming;
    private Integer attended;
    private Integer missed;
    private Integer canceled;
    private Integer totalDoctors;
}
