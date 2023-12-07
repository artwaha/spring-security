package orci.or.tz.appointments.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ADResponseDto {
    private String access_token;
    private Integer expires_in;
    private Integer refresh_expires_in;
    private String refresh_token;
    private String token_type;
    @JsonProperty("not-before-policy")
    private Integer not_before_policy;
    private String session_state;
    private String scope;
}
