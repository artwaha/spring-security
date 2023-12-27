package orci.or.tz.appointments.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

@Service
public class SecurityAuthenticationService {

    public String getUsername() {
        // Retrieve the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            // Get the username from the authentication object
            String username = authentication.getName();
            return username;
        } else {
            return null;
        }
    }

}
