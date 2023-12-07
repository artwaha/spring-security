package orci.or.tz.appointments.security;


import lombok.extern.slf4j.Slf4j;
import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.repository.PatientRepository;
import orci.or.tz.appointments.utilities.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;



@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private PatientRepository userRepository;


    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        Object credentials = authentication.getCredentials();

        if (!(credentials instanceof String)) {
            return null;
        }
        String password = credentials.toString();

        ApplicationUser user = userRepository.findByRegistrationNumber(name).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password provided");
        }
        return authenticateByPassType(name, password, user);
    }

    private Authentication authenticateByPassType(String username, String password, ApplicationUser user) {
        AppUserDetails userDetails = null;

        try{
            userDetails = new AppUserDetails(user.getRegistrationNumber(), null, user.getId(),user.getRegistrationNumber(),user.getFullName(), user.getGender(),user.getMobile(),user.getDob(),user.getStatus());

            return new UsernamePasswordAuthenticationToken(userDetails, password, null);
        } catch (Exception e){
            log.error("Wrong credentials {}", username, password);
            throw new UsernameNotFoundException("Invalid username or password provided");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
