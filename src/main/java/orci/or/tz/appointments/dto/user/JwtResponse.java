package orci.or.tz.appointments.dto.user;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class JwtResponse {
	private String token;

	private String refreshToken;

	private Date expireTime;
	private String type = "Bearer";
	private Long id;
	private String username;
	private String email;


	private List<String> roles;


	

	public JwtResponse(String accessToken, Long id, String userName, String refreshToken, Date expireTime) {
		this.token = accessToken;
		this.id = id;
		this.username = userName;
		this.refreshToken = refreshToken;
		this.expireTime = expireTime;


	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public String getToken() {
		return token;
	}

	public String getType() {
		return type;
	}

	public Long getId() {
		return id;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}


	public String getUserName() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}
}
