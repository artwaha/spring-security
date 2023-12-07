package orci.or.tz.appointments.utilities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import orci.or.tz.appointments.enums.GenderEnum;
import orci.or.tz.appointments.models.ApplicationUser;

import java.time.LocalDate;


@Getter
@Setter
@ToString
public class AppUserDetails extends ApplicationUser {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String registrationNumber;

    private String username;
    private String fullName;
    private String mobile;
    private GenderEnum gender;
    private LocalDate dob;
    private String billingCategory;
    private String status;



    public AppUserDetails(String username, String password, Long userId, String registrationNumber, String fullName, GenderEnum gender, String mobile, LocalDate dob, String status) {
        super(username, password);
        this.username = username;
        this.id = userId;
        this.fullName = fullName;
        this.registrationNumber = registrationNumber;
        this.gender = gender;
        this.mobile= mobile;
        this.dob=dob;
        this.status=status;


    }

    public AppUserDetails() {
        super("", "");
    }
}
