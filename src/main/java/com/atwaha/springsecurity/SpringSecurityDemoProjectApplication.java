package com.atwaha.springsecurity;

import com.atwaha.springsecurity.model.User;
import com.atwaha.springsecurity.model.dto.RegisterRequest;
import com.atwaha.springsecurity.repository.UserRepository;
import com.atwaha.springsecurity.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

import static com.atwaha.springsecurity.model.enums.Role.ADMIN;
import static com.atwaha.springsecurity.model.enums.Role.USER;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringSecurityDemoProjectApplication implements ApplicationRunner {
    private final UserRepository userRepository;
    private final AuthService authService;

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityDemoProjectApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<User> admin = userRepository.findByEmailIgnoreCase("admin@domain.com");
        Optional<User> sherlock = userRepository.findByEmailIgnoreCase("sholmes@domain.com");
        if (admin.isPresent())
            return;
        if (sherlock.isPresent())
            return;

        RegisterRequest adminRequest = RegisterRequest
                .builder()
                .fullName("Admin")
                .email("admin@domain.com")
                .role(ADMIN)
                .password("admin")
                .build();

        RegisterRequest sherlockRequest = RegisterRequest
                .builder()
                .fullName("Sherlock Holmes")
                .email("sholmes@domain.com")
                .role(USER)
                .password("123")
                .build();

        String adminAccessToken = authService.register(adminRequest).getToken();
        String sherlockAccessToken = authService.register(sherlockRequest).getToken();

        System.out.println("Admin Access Token => " + adminAccessToken);
        System.out.println("Sherlock Access Token => " + sherlockAccessToken);
    }
}
