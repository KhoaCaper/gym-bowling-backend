package com.drugprevention.gymbowlingbackend.config;

import com.drugprevention.gymbowlingbackend.security.FirebaseAuthenticationFilter;
import com.drugprevention.gymbowlingbackend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpMethod;

// TEMPORARILY DISABLED FOR TEAM TESTING
// @Configuration
// @EnableWebSecurity
public class SecurityConfig {

    // TEMPORARILY DISABLE ALL AUTH FOR FASTEST TEAM TESTING
    // private final FirebaseAuthenticationFilter firebaseAuthenticationFilter;
    // private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // public SecurityConfig(FirebaseAuthenticationFilter firebaseAuthenticationFilter) {
    //     this.firebaseAuthenticationFilter = firebaseAuthenticationFilter;
    //     // this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // COMPLETELY DISABLE SECURITY FOR TEAM TESTING - FASTEST WAY
        http.cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
        
        return http.build();
    }

    // DISABLE CORS COMPLETELY FOR FASTEST TESTING
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false); // Disable for fastest testing
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
