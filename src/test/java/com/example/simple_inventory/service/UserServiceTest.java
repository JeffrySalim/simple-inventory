package com.example.simple_inventory.service;

import com.example.simple_inventory.entity.RegisterRequest;
import com.example.simple_inventory.entity.RegisterResponse;
import com.example.simple_inventory.entity.Role;
import com.example.simple_inventory.entity.User;
import com.example.simple_inventory.repository.RoleRepository;
import com.example.simple_inventory.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void register_returnRegisterResponse_whenInputValid() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");
        request.setPassword("1234");
        request.setFullName("John Doe");
        request.setEmail("john@example.com");
        request.setRole("USER");

        Role role = new Role();
        role.setId(1L);
        role.setName("USER");

        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("1234")).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("john");
        savedUser.setRole(role);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegisterResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals("john", response.getUsername());
        assertEquals("USER", response.getRole());
    }

    @Test
    void register_throwException_whenUsernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> userService.register(request));
    }

    @Test
    void register_throwException_whenRoleInvalid() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("jane");
        request.setRole("INVALID");

        when(userRepository.findByUsername("jane")).thenReturn(Optional.empty());
        when(roleRepository.findByName("INVALID")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.register(request));
    }
}