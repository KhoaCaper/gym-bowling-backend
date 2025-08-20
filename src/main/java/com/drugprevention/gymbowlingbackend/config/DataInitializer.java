package com.drugprevention.gymbowlingbackend.config;

import com.drugprevention.gymbowlingbackend.entity.ServiceType;
import com.drugprevention.gymbowlingbackend.entity.Role;
import com.drugprevention.gymbowlingbackend.repository.ServiceTypeRepository;
import com.drugprevention.gymbowlingbackend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private final ServiceTypeRepository serviceTypeRepository;
    private final RoleRepository roleRepository;

    public DataInitializer(ServiceTypeRepository serviceTypeRepository,
                          RoleRepository roleRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.roleRepository = roleRepository;
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // Initialize Roles if they don't exist
            if (roleRepository.count() == 0) {
                Role adminRole = new Role("ADMIN", "System administrator with full access");
                Role staffRole = new Role("STAFF", "Staff member with center management access");
                Role userRole = new Role("USER", "Regular user with basic access");
                
                roleRepository.save(adminRole);
                roleRepository.save(staffRole);
                roleRepository.save(userRole);
                
                System.out.println("Roles initialized successfully!");
            }
            
            // Initialize ServiceTypes if they don't exist
            if (serviceTypeRepository.count() == 0) {
                ServiceType gym = new ServiceType("GYM", "Gym and fitness services");
                ServiceType bowling = new ServiceType("BOWLING", "Bowling and entertainment services");
                
                serviceTypeRepository.save(gym);
                serviceTypeRepository.save(bowling);
                
                System.out.println("ServiceTypes initialized successfully!");
            }
            
            System.out.println("Database initialization completed!");
        };
    }
}
