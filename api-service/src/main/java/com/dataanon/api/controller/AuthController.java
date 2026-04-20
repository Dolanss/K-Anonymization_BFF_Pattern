package com.dataanon.api.controller;

import com.dataanon.api.domain.entity.User;
import com.dataanon.api.dto.auth.LoginRequest;
import com.dataanon.api.dto.auth.LoginResponse;
import com.dataanon.api.dto.auth.UserInfoDto;
import com.dataanon.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate and receive a JWT token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user info")
    public ResponseEntity<UserInfoDto> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(UserInfoDto.from(user));
    }
}
