package orci.or.tz.appointments.web.external;


import orci.or.tz.appointments.dto.user.JwtResponse;
import orci.or.tz.appointments.dto.user.LoginRequest;
import orci.or.tz.appointments.exceptions.OperationFailedException;
import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.models.RefreshToken;
import orci.or.tz.appointments.security.jwt.JwtTokenUtil;
import orci.or.tz.appointments.services.PatientService;
import orci.or.tz.appointments.services.RefreshTokenService;
import orci.or.tz.appointments.utilities.AppUserDetails;
import orci.or.tz.appointments.web.external.api.LoginApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.Optional;


@RestController
public class LoginController implements LoginApi {


    @Autowired
    private PatientService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;
    
    @Override
    public ResponseEntity<?> UserLogin(LoginRequest request) throws IOException, OperationFailedException {


            Optional<ApplicationUser> usr = userService.GetPatientByRegistrationNumber(request.getRegistrationNumber());

            if(usr.isPresent()) {

                if(usr.get().getOtp().equals(request.getOtp())) {
                    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getRegistrationNumber(), request.getOtp()));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    final AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
                    ApplicationUser user = usr.get();


                    final String token = jwtUtils.generateToken(user);
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
                    JwtResponse resp = new JwtResponse(token, userDetails.getId(), user.getRegistrationNumber(), refreshToken.getToken(), jwtUtils.getExpirationDateFromToken(token));

                    return ResponseEntity.ok(resp);
                }else{
                    throw  new OperationFailedException("Invalid Username Or Password");
                }
            }else{
                throw  new OperationFailedException("Invalid Username Or Password");
            }



    }
}

