package com.drugprevention.gymbowlingbackend.config;

import com.drugprevention.gymbowlingbackend.entity.Center;
import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import com.drugprevention.gymbowlingbackend.entity.ServiceType;
import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.entity.Role;
import com.drugprevention.gymbowlingbackend.repository.CenterRepository;
import com.drugprevention.gymbowlingbackend.repository.PackagePlanRepository;
import com.drugprevention.gymbowlingbackend.repository.ServiceTypeRepository;
import com.drugprevention.gymbowlingbackend.repository.UserRepository;
import com.drugprevention.gymbowlingbackend.repository.RoleRepository;
import com.drugprevention.gymbowlingbackend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
public class DataInitializer {

    private final PackagePlanRepository packagePlanRepository;
    private final CenterRepository centerRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PackagePlanRepository packagePlanRepository, 
                          CenterRepository centerRepository,
                          ServiceTypeRepository serviceTypeRepository,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          UserService userService,
                          PasswordEncoder passwordEncoder) {
        this.packagePlanRepository = packagePlanRepository;
        this.centerRepository = centerRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
            
            // Only create sample data if no packages exist
            if (packagePlanRepository.count() == 0) {
                // Get default center (Center A - ID 1)
                Optional<Center> defaultCenterOptional = centerRepository.findById(1L);
                
                if (defaultCenterOptional.isPresent()) {
                    Center defaultCenter = defaultCenterOptional.get();
                    
                    PackagePlan basic = new PackagePlan(
                        "Gói Cơ Bản", 
                        "Gói tập gym cơ bản với đầy đủ thiết bị", 
                        new BigDecimal("500000"), 
                        1, // durationMonths: 1 tháng
                        defaultCenter // center
                    );
                    
                    PackagePlan premium = new PackagePlan(
                        "Gói Premium", 
                        "Gói tập gym + bowling + HLV cá nhân", 
                        new BigDecimal("1200000"), 
                        3, // durationMonths: 3 tháng
                        defaultCenter // center
                    );
                    
                    PackagePlan vip = new PackagePlan(
                        "Gói VIP", 
                        "Gói cao cấp với mọi dịch vụ + spa + dinh dưỡng", 
                        new BigDecimal("2500000"), 
                        6, // durationMonths: 6 tháng
                        defaultCenter // center
                    );
                    
                    packagePlanRepository.save(basic);
                    packagePlanRepository.save(premium);
                    packagePlanRepository.save(vip);
                    
                    System.out.println("Sample package data created successfully!");
                } else {
                    System.out.println("Warning: Default Center with ID 1 not found. Sample PackagePlans not initialized.");
                }
            }
            
            // Insert sample admin user
            if (userRepository.count() == 0) {
                Optional<Role> adminRoleOptional = roleRepository.findByName("ADMIN");
                
                if (adminRoleOptional.isPresent()) {
                    Role adminRole = roleRepository.findByName("ADMIN").get();
                    
                    // Create admin user manually with fake firebase_uid to avoid constraint issue
                    String encodedPassword = passwordEncoder.encode("admin123");
                    String fakeFirebaseUid = "system_admin_" + System.currentTimeMillis();
                    
                    User adminUser = new User(
                        "admin", 
                        encodedPassword, 
                        fakeFirebaseUid, // Use fake firebase_uid instead of null
                        "admin@gymbowling.com", 
                        "System Administrator", 
                        "0901234567"
                    );
                    adminUser.setRole(adminRole);
                    adminUser.setIsActive(true);
                    adminUser.setCreatedAt(LocalDateTime.now());
                    adminUser.setUpdatedAt(LocalDateTime.now());
                    userRepository.save(adminUser);
                    
                    System.out.println("Admin user created successfully with encoded password!");
                } else {
                    System.out.println("Warning: ADMIN role not found. Admin user not created.");
                }
            }
        };
    }
}
