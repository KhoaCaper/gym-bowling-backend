package com.drugprevention.gymbowlingbackend.config;

import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import com.drugprevention.gymbowlingbackend.repository.PackagePlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(PackagePlanRepository packagePlanRepository) {
        return args -> {
            // Only create sample data if no packages exist
            if (packagePlanRepository.count() == 0) {
                
                PackagePlan basic = new PackagePlan(
                    "Gói Cơ Bản", 
                    "Gói tập gym cơ bản với đầy đủ thiết bị", 
                    new BigDecimal("500000"), 
                    1
                );
                
                PackagePlan premium = new PackagePlan(
                    "Gói Premium", 
                    "Gói tập gym + bowling + HLV cá nhân", 
                    new BigDecimal("1200000"), 
                    3
                );
                
                PackagePlan vip = new PackagePlan(
                    "Gói VIP", 
                    "Gói cao cấp với mọi dịch vụ + spa + dinh dưỡng", 
                    new BigDecimal("2500000"), 
                    6
                );
                
                packagePlanRepository.save(basic);
                packagePlanRepository.save(premium);
                packagePlanRepository.save(vip);
                
                System.out.println("Sample package data created!");
            }
        };
    }
}
