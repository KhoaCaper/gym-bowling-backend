package com.drugprevention.gymbowlingbackend.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    @Autowired(required = false)
    private FirebaseAuth firebaseAuth;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // Skip Firebase authentication if Firebase is disabled
        if (firebaseAuth == null) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            try {
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
                String uid = decodedToken.getUid();
                String email = decodedToken.getEmail();
                
                // Get custom claims for roles
                String role = (String) decodedToken.getClaims().getOrDefault("role", "USER");
                
                List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + role)
                );
                
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(uid, null, authorities);
                
                authentication.setDetails(decodedToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
            } catch (Exception e) {
                System.err.println("Firebase token verification failed: " + e.getMessage());
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
