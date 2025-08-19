package com.drugprevention.gymbowlingbackend.config;

import com.drugprevention.gymbowlingbackend.entity.Center;
import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import com.drugprevention.gymbowlingbackend.repository.CenterRepository;
import com.drugprevention.gymbowlingbackend.repository.PackagePlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Optional;

@Configuration
public class DataInitializer {

    private final PackagePlanRepository packagePlanRepository;
    private final CenterRepository centerRepository;

    public DataInitializer(PackagePlanRepository packagePlanRepository, CenterRepository centerRepository) {
        this.packagePlanRepository = packagePlanRepository;
        this.centerRepository = centerRepository;
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
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
        };
    }
}
