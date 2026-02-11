package com.example.simple_inventory.service;

import com.example.simple_inventory.entity.Role;
import com.example.simple_inventory.entity.User;
import com.example.simple_inventory.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsername_returnUserDetails_whenUserExists() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("encodedPassword");
        Role role = new Role();
        role.setName("USER");
        user.setRole(role);

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("john");

        assertEquals("john", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_throwException_whenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("unknown"));
    }
}