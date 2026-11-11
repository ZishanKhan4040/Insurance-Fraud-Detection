
package com.motorinsurance.frauddetectionsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.motorinsurance.frauddetectionsystem.dto.RegisterRequest;
import com.motorinsurance.frauddetectionsystem.dto.LoginRequest;
import com.motorinsurance.frauddetectionsystem.dto.AuthResponse;
import com.motorinsurance.frauddetectionsystem.entity.User;
import com.motorinsurance.frauddetectionsystem.security.JwtService;
import com.motorinsurance.frauddetectionsystem.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(
            AuthService authService,
            JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Registration successful. User ID: "
                            + user.getUserId());

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {
        try {
            User user = authService.login(request);

            String token = jwtService.generateToken(user.getEmail());

            AuthResponse response = new AuthResponse();
            response.setUserId(user.getUserId());
            response.setFullName(user.getFullName());
            response.setEmail(user.getEmail());
            response.setRole(user.getRole().name());
            response.setToken(token);
            response.setMessage("Login successful");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
    }
}
