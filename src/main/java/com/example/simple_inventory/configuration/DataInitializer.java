package com.example.simple_inventory.configuration;

import com.example.simple_inventory.entity.Inventory;
import com.example.simple_inventory.entity.Role;
import com.example.simple_inventory.entity.User;
import com.example.simple_inventory.repository.InventoryRepository;
import com.example.simple_inventory.repository.RoleRepository;
import com.example.simple_inventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner init() {
        return args -> {
            // Akun ADMIN awal
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN")));

            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(new Role(null, "USER")));

            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // hash password
                admin.setFullName("Admin");
                admin.setEmail("admin@gmail.com");
                admin.setRole(adminRole);
                userRepository.save(admin);
            }
        };
    }
}

