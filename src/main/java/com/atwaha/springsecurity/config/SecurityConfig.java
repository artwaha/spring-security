package com.atwaha.springsecurity.config;

import com.atwaha.springsecurity.security.CustomAccessDeniedHandler;
import com.atwaha.springsecurity.security.CustomLogoutHandler;
import com.atwaha.springsecurity.security.CustomUserDetailsService;
import com.atwaha.springsecurity.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.atwaha.springsecurity.model.enums.Role.ADMIN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomLogoutHandler logoutHandler;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        /* This is for H2 Database Console */
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.authorizeHttpRequests(
                requests -> {
                    requests
                            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/v1/auth/login", "/h2-console/**")
                            .permitAll();
                    requests
                            .requestMatchers("/api/v1/auth/register")
                            .hasAuthority(ADMIN.name());
                    requests
                            .anyRequest()
                            .authenticated();
                }
        );
//        http.userDetailsService(userDetailsService);
        http.sessionManagement(session -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        http.exceptionHandling(exceptionHandling -> {
            exceptionHandling.accessDeniedHandler(accessDeniedHandler);
            exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                /* No Token or Token is Invalid */
                response.setStatus(UNAUTHORIZED.value());

            });
        });
        http.logout(logout -> {
            logout.logoutUrl("/api/v1/auth/logout");
            logout.addLogoutHandler(logoutHandler);
            logout.logoutSuccessHandler((request, response, authentication) -> {
                response.setStatus(HttpStatus.OK.value());
            });
        });
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
