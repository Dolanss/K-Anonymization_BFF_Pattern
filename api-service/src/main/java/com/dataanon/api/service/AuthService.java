package com.dataanon.api.service;

import com.dataanon.api.domain.entity.User;
import com.dataanon.api.dto.auth.LoginRequest;
import com.dataanon.api.dto.auth.LoginResponse;
import com.dataanon.api.dto.auth.UserInfoDto;
import com.dataanon.api.repository.UserRepository;
import com.dataanon.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> claims = Map.of(
                "role", user.getRole().name(),
                "companyId", user.getCompany() != null ? user.getCompany().getId() : ""
        );

        String token = jwtService.generateToken(user, claims);
        return new LoginResponse(token, UserInfoDto.from(user));
    }
}
