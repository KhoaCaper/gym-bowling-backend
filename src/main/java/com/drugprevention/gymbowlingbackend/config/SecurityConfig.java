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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final FirebaseAuthenticationFilter firebaseAuthenticationFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(FirebaseAuthenticationFilter firebaseAuthenticationFilter, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.firebaseAuthenticationFilter = firebaseAuthenticationFilter;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll() // Allow Swagger
                .requestMatchers("/api/auth/**").permitAll() // Allow authentication endpoints
                .requestMatchers("/api/firebase-auth/**").permitAll() // Allow Firebase auth
                .requestMatchers("/api/public/**").permitAll() // Allow public endpoints
                .requestMatchers("/api/cors-test/**").permitAll() // Allow CORS testing
                
                // PUBLIC ENDPOINTS - Không cần đăng nhập
                .requestMatchers("/api/package-plans/**").permitAll() // View packages - PUBLIC
                .requestMatchers("/api/packages/**").permitAll() // View packages - PUBLIC
                .requestMatchers("/api/centers/**").permitAll() // View centers - PUBLIC
                .requestMatchers("/api/service-types/**").permitAll() // View service types - PUBLIC
                .requestMatchers("/api/time-frames/**").permitAll() // View time frames - PUBLIC
                
                // PROTECTED ENDPOINTS - Cần đăng nhập
                .requestMatchers("/api/users/**").authenticated() // User management - PROTECTED
                .requestMatchers("/api/orders/**").authenticated() // Order management - PROTECTED
                .requestMatchers("/api/payments/**").authenticated() // Payment - PROTECTED
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Admin only - PROTECTED
                .requestMatchers("/api/staff/**").authenticated() // Staff endpoints - PROTECTED
                
                .anyRequest().authenticated() // Default: require authentication
            )
            .addFilterBefore(firebaseAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow specific origins for frontend - ENHANCED FOR PRODUCTION
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:3000",           // React development
            "http://localhost:5173",           // Vite development
            "http://localhost:8080",           // Local backend
            "https://*.ngrok-free.app",        // Ngrok testing - ALL subdomains
            "https://*.ngrok.io",              // Ngrok alternative domains
            "https://*.railway.app",           // Railway domains
            "https://gym-bowling-backend-production.up.railway.app", // Main Railway URL
            "https://*.vercel.app",            // Vercel domains
            "https://*.netlify.app"            // Netlify domains
        ));
        
        // Allow ALL methods for production
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        
        // Allow ALL headers for production
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Allow credentials for JWT
        configuration.setAllowCredentials(true);
        
        // Expose ALL headers for production
        configuration.setExposedHeaders(Arrays.asList("*"));
        
        // Set max age for preflight requests
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
