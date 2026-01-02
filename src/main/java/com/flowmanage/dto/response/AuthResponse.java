package com.flowmanage.dto.response;

import lombok.Getter;

@Getter
public class AuthResponse {

    private final String accessToken;
    private final String tokenType = "Bearer";

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
