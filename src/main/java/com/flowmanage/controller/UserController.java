package com.flowmanage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowmanage.dto.response.ApiResponse;
import com.flowmanage.security.AuthenticatedUser;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ApiResponse<String> me(Authentication authentication) {
        AuthenticatedUser user =
            (AuthenticatedUser) authentication.getPrincipal();

        return new ApiResponse<>("Hello " + user.getEmail());
    }
}
