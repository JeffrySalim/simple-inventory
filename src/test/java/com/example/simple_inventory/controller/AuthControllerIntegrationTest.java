package com.example.simple_inventory.controller;

import com.example.simple_inventory.entity.RegisterRequest;
import com.example.simple_inventory.entity.Role;
import com.example.simple_inventory.entity.User;
import com.example.simple_inventory.repository.RoleRepository;
import com.example.simple_inventory.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIntegrationTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper(); // manual

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Hapus semua data untuk mencegah duplicate entry
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Pastikan role USER ada
        Role userRole = new Role();
        userRole.setName("USER");
        roleRepository.save(userRole);
    }

    // POSITIF: registrasi berhasil
    @Test
    void registerUser_returnCreated_whenValid() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user1");
        request.setPassword("password123");
        request.setFullName("User One");
        request.setEmail("user1@example.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Registrasi berhasil"))
                .andExpect(jsonPath("$.data.username").value("user1"))
                .andExpect(jsonPath("$.data.role").value("USER"));
    }

    // NEGATIF: username sudah ada
    @Test
    void registerUser_returnError_whenUsernameExists() throws Exception {
        // Simpan user dulu
        User existing = new User();
        existing.setUsername("user2");
        existing.setPassword("password");
        existing.setFullName("Existing User");
        existing.setEmail("existing@example.com");
        existing.setRole(roleRepository.findByName("USER").get()); // wajib set role
        userRepository.save(existing);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("user2"); // sama
        request.setPassword("password123");
        request.setFullName("User Two");
        request.setEmail("user2@example.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(result ->
                        result.getResolvedException().getMessage().contains("Username sudah ada"));
    }

    // NEGATIF: role di input berbeda, tapi harus USER
    @Test
    void registerUser_returnUserRoleAlways_USER() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user3");
        request.setPassword("password123");
        request.setFullName("User Three");
        request.setEmail("user3@example.com");
        request.setRole("ADMIN"); // coba override

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.role").value("USER")); // tetap USER
    }
}