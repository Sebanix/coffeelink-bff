package com.coffeelink.bff.dto;

import lombok.Data;

@Data
public class AuthResponse {

    private String token;
    private String rol;

    public AuthResponse(String token, String rol) {
        this.token = token;
        this.rol = rol;
    }
}
