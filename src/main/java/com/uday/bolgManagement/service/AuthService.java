package com.uday.bolgManagement.service;

import com.uday.bolgManagement.dto.AuthResponse;
import com.uday.bolgManagement.dto.LoginRequest;
import com.uday.bolgManagement.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    String register(RegisterRequest registerRequest);
}
