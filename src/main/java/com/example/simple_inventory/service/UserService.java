package com.example.simple_inventory.service;

import com.example.simple_inventory.entity.RegisterRequest;
import com.example.simple_inventory.entity.RegisterResponse;
import com.example.simple_inventory.entity.Role;
import com.example.simple_inventory.entity.User;
import com.example.simple_inventory.repository.RoleRepository;
import com.example.simple_inventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Method register untuk AuthController
    public RegisterResponse register(RegisterRequest request) {

        // Cek username sudah ada
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username sudah ada");
        }

        // Ambil role dari DB
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role tidak valid"));

        // Buat user baru
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(role);

        // 4. Simpan ke DB
        User savedUser = userRepository.save(user);

        // 5. Return response
        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole().getName()
        );
    }
}
