package com.atwaha.springsecurity.controller;

import com.atwaha.springsecurity.model.dto.AuthResponse;
import com.atwaha.springsecurity.model.dto.LoginRequest;
import com.atwaha.springsecurity.model.dto.RegisterRequest;
import com.atwaha.springsecurity.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthService authService;

    @SecurityRequirement(name = "jba")
    @PostMapping("register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(registerRequest));
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(loginRequest));
    }
}
