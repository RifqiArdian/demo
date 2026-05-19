package com.example.demo.web.controller;

import com.example.demo.domain.model.User;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.infrastructure.repository.JpaUserRepository;
import com.example.demo.web.dto.LoginRequest;
import com.example.demo.web.dto.WebResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"jwt.secret=testjwtsecretkeymachinelearningusedinexamplesonly123"})
@DisplayName("AuthController Integration Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        jpaUserRepository.deleteAll();
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
    }

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Register berhasil"))
                .andExpect(jsonPath("$.data").value("testuser"));
    }

    @Test
    @DisplayName("Should fail register with duplicate username")
    void testRegisterDuplicateUsername() throws Exception {
        // Arrange - Create existing user
        User existingUser = new User();
        existingUser.setUsername("testuser");
        existingUser.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(existingUser);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username sudah digunakan"));
    }

    @Test
    @DisplayName("Should login successfully and return JWT token")
    void testLoginSuccess() throws Exception {
        // Arrange - Create user first
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(user);

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login berhasil"))
                .andExpect(jsonPath("$.data.token").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        WebResponse<?> webResponse = objectMapper.readValue(responseBody, WebResponse.class);
        assertNotNull(webResponse.getData());
    }

    @Test
    @DisplayName("Should fail login with invalid password")
    void testLoginInvalidPassword() throws Exception {
        // Arrange - Create user with different password
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("differentPassword"));
        userRepository.save(user);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Username atau password salah!"));
    }

    @Test
    @DisplayName("Should fail login with non-existent user")
    void testLoginUserNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Username atau password salah!"));
    }

    @Test
    @DisplayName("Should fail register with empty username")
    void testRegisterEmptyUsername() throws Exception {
        // Arrange
        loginRequest.setUsername("");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail register with empty password")
    void testRegisterEmptyPassword() throws Exception {
        // Arrange
        loginRequest.setPassword("");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }
}
