package com.jwtly10.clientservice.service.auth;

import com.jwtly10.clientservice.api.auth.AuthResponse;
import com.jwtly10.clientservice.api.auth.AuthRequest;
import com.jwtly10.common.models.Role;
import com.jwtly10.common.models.User;
import com.jwtly10.databaseservice.dao.UserDAOImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDAOImpl userDAOImpl;

    public AuthResponse register(AuthRequest request) {
        var user = User.builder()
                .first_name(request.getFirstname())
                .username(request.getUsername())
                .last_name(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userDAOImpl.create(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userDAOImpl.get(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
