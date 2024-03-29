package com.atwaha.springsecurity.service;

import com.atwaha.springsecurity.Utils;
import com.atwaha.springsecurity.model.User;
import com.atwaha.springsecurity.model.dto.AuthResponse;
import com.atwaha.springsecurity.model.dto.LoginRequest;
import com.atwaha.springsecurity.model.dto.RegisterRequest;
import com.atwaha.springsecurity.repository.UserRepository;
import com.atwaha.springsecurity.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final Utils utils;

    public AuthResponse register(RegisterRequest registerRequest) {
        User newUser = modelMapper.map(registerRequest, User.class);
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User savedUser = userRepository.save(newUser);

        String accessToken = jwtService.generateToken(savedUser, utils.getExtraClaims(savedUser));

        return AuthResponse
                .builder()
                .token(accessToken)
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
                );
        User user = userRepository.findByEmailIgnoreCase(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        String accessToken = jwtService.generateToken(user, utils.getExtraClaims(user));

        return AuthResponse
                .builder()
                .token(accessToken)
                .build();
    }
}
