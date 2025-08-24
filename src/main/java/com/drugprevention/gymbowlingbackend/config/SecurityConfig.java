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
                
                // HEALTH CHECK ENDPOINTS - Không cần đăng nhập
                .requestMatchers("/").permitAll() // Root healthcheck for Railway
                .requestMatchers("/health").permitAll() // Health endpoint for monitoring
                
                // PUBLIC ENDPOINTS - Không cần đăng nhập (chỉ xem)
                .requestMatchers(HttpMethod.GET, "/api/package-plans/**").permitAll() // View packages - PUBLIC
                .requestMatchers(HttpMethod.GET, "/api/package-plan-details/**").permitAll() // View package details - PUBLIC
                .requestMatchers(HttpMethod.GET, "/api/packages/**").permitAll() // View packages - PUBLIC
                .requestMatchers(HttpMethod.GET, "/api/centers/**").permitAll() // View centers - PUBLIC
                .requestMatchers(HttpMethod.GET, "/api/service-types/**").permitAll() // View service types - PUBLIC
                .requestMatchers(HttpMethod.GET, "/api/timeframes/**").permitAll() // View time frames - PUBLIC
                
                // STAFF ENDPOINTS - Chỉ STAFF mới được tạo/sửa/xóa
                .requestMatchers(HttpMethod.POST, "/api/package-plans/**").hasRole("STAFF") // Create packages - STAFF ONLY
                .requestMatchers(HttpMethod.PUT, "/api/package-plans/**").hasRole("STAFF") // Update packages - STAFF ONLY
                .requestMatchers(HttpMethod.DELETE, "/api/package-plans/**").hasRole("STAFF") // Delete packages - STAFF ONLY
                .requestMatchers(HttpMethod.POST, "/api/package-plan-details/**").hasRole("STAFF") // Create package details - STAFF ONLY
                .requestMatchers(HttpMethod.PUT, "/api/package-plan-details/**").hasRole("STAFF") // Update package details - STAFF ONLY
                .requestMatchers(HttpMethod.DELETE, "/api/package-plan-details/**").hasRole("STAFF") // Delete package details - STAFF ONLY
                
                // PROTECTED ENDPOINTS - Cần đăng nhập
                .requestMatchers("/api/users/**").authenticated() // User management - PROTECTED
                .requestMatchers("/api/orders/**").authenticated() // Order management - PROTECTED
                .requestMatchers("/api/payments/**").authenticated() // Payment - PROTECTED
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Admin only - PROTECTED
                .requestMatchers("/api/staff/**").authenticated() // Staff endpoints - PROTECTED
                
                .anyRequest().authenticated() // Default: require authentication
            );
        
        // Add Firebase filter only if it's available
        if (firebaseAuthenticationFilter != null) {
            http.addFilterBefore(firebaseAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }
        
        // Add JWT filter
        if (jwtAuthenticationFilter != null) {
            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // ULTRA SIMPLE CORS for learning and deployment
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
