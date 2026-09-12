package com.uday.bolgManagement.security;

import com.uday.bolgManagement.model.User;
import com.uday.bolgManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(()->userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(()-> new UsernameNotFoundException("Username not found with Username or Email:"+usernameOrEmail));

        return new CustomUserDetails(user);
    }
}
