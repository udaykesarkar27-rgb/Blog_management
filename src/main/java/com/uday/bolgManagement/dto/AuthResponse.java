package com.uday.bolgManagement.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        String username,
        String role
) {
    public  AuthResponse(String accessToken , String username, String role){
        this (accessToken,"Bearer",username,role);
    }
}
