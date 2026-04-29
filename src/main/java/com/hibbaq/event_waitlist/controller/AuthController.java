package com.hibbaq.event_waitlist.controller;

import com.hibbaq.event_waitlist.dto.LoginRequest;
import com.hibbaq.event_waitlist.dto.RegisterRequest;
import com.hibbaq.event_waitlist.dto.AuthResponse;
import com.hibbaq.event_waitlist.model.User;
import com.hibbaq.event_waitlist.service.AuthService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        User user = authService.register(
            request.getEmail(),
            request.getName(),
            request.getPassword(),
            request.getRole()
        );
        return new AuthResponse(null, "Registration successful", user.getId(), user.getRole().toString());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        User user = authService.login(request.getEmail(), request.getPassword());
        return new AuthResponse(null, "Login successful", user.getId(), user.getRole().toString());
    }
}