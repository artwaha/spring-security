package orci.or.tz.appointments.security.jwt;



import io.jsonwebtoken.ExpiredJwtException;

import orci.or.tz.appointments.services.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtUtils;

    @Autowired
    private PatientService userDetailsService;


    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        //System.out.println("Request Param Map "+request.getParameterMap().keySet().toString());
        //System.out.println("Request Authorization Header "+request.getHeader("Authorization"));

        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if ((requestTokenHeader != null && requestTokenHeader.startsWith("Bearer "))) {
			jwtToken = requestTokenHeader.split(" ")[1];
			try {
                username = jwtUtils.getUsernameFromToken(jwtToken);
                logger.info(username);
            } catch (IllegalArgumentException e) {
                logger.info("Unable to get JWT Token");
                //throw new AuthenticationCredentialsNotFoundException("Authentication Failed");
            } catch (ExpiredJwtException e) {
                logger.info("JWT Token has expired");
                e.printStackTrace();
                //throw new AuthenticationCredentialsNotFoundException("Authentication Failed");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
            //throw new AuthenticationCredentialsNotFoundException("Authentication Failed");
        }


        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.LoadUserByRegno(username);

            // if token is valid configure Spring Security to manually set
            // authentication
            if (jwtUtils.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(request, response);
    }

}
