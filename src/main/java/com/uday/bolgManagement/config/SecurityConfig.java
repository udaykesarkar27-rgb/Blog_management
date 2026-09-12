package com.uday.bolgManagement.config;

import com.uday.bolgManagement.security.jwtAuthenticationEntryPoint;

import com.uday.bolgManagement.security.jwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.internal.Abstract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final jwtAuthenticationEntryPoint authenticationEntryPoint;
    private final jwtAuthenticationFilter authenticationFilter;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception->exception.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize->authorize
                    //Public Auth EndPoints
                      .requestMatchers("/api/auth/**").permitAll()
                    //Public Post & Comment Read Access
                        .requestMatchers(HttpMethod.GET,"/api/posts/**").permitAll()
                    //Swagger Ui and Documentation
                        .requestMatchers("/v3/api-docs/**","/swagger-ui/**","swagger-ui.html").permitAll()
                     //Secured Actions
                        .requestMatchers(HttpMethod.POST,"/api/posts/**").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/api/posts/**").authenticated()
                        .requestMatchers("/api/commnts/**").authenticated()
                    //Admin-Only post Deletion
                        .requestMatchers(HttpMethod.DELETE,"/api/posts/**").authenticated()
                     //Any Other Request Must be Authenticated
                        .anyRequest().authenticated()
                );
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:3000","http://localhost:5173","http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET","PUT","POST","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization","content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}
