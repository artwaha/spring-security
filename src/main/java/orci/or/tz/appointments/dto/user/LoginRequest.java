package orci.or.tz.appointments.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
	@NotBlank
	private String registrationNumber;

	@NotBlank
	private String otp;

}
