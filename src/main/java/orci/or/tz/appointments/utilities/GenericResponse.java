package orci.or.tz.appointments.utilities;

import lombok.Data;

@Data
public class GenericResponse <T>{

    private Integer currentPage;
    private Integer totalItems;
    private Integer totalPages;
    private Integer pageSize;
    private T data;
}
