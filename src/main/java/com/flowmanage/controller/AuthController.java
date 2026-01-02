package com.flowmanage.controller;

import com.flowmanage.dto.request.LoginRequest;
import com.flowmanage.dto.request.RegisterRequest;
import com.flowmanage.dto.response.ApiResponse;
import com.flowmanage.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return new ApiResponse<>("User registered successfully");
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return new ApiResponse<>(token);
    }
}
