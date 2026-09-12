package com.uday.bolgManagement.service.Impl;

import com.uday.bolgManagement.dto.AuthResponse;
import com.uday.bolgManagement.dto.LoginRequest;
import com.uday.bolgManagement.dto.RegisterRequest;
import com.uday.bolgManagement.exception.BadRequestException;
import com.uday.bolgManagement.model.Role;
import com.uday.bolgManagement.model.User;
import com.uday.bolgManagement.repository.UserRepository;
import com.uday.bolgManagement.security.JwtTokenProvider;
import com.uday.bolgManagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.usernameOrEmail(),
                        loginRequest.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        User user =userRepository.findByUsername(authentication.getName())
                .or(()->userRepository.findByEmail(authentication.getName()))
                .orElseThrow(()-> new BadRequestException("User na=ot Found"));
        return new AuthResponse(token,user.getUsername(),user.getRole().name());
    }

    @Override
    public String register(RegisterRequest registerRequest){
        if (userRepository.existsByUsername(registerRequest.username())){
            throw new BadRequestException("Email is Already on use! ");
        }

        User user = User.builder()
                .username(registerRequest.username())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .role(Role.ROLE_User)
                .build();

        userRepository.save(user);

        return "User Registered Successfully!";
    }
}
