package com.example.demo.web.controller;

import com.example.demo.domain.model.Product;
import com.example.demo.domain.model.User;
import com.example.demo.domain.repository.ProductRepository;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.infrastructure.repository.JpaProductRepository;
import com.example.demo.infrastructure.repository.JpaUserRepository;
import com.example.demo.infrastructure.security.JwtService;
import com.example.demo.web.dto.ProductRequest;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"jwt.secret=testjwtsecretkeymachinelearningusedinexamplesonly123"})
@DisplayName("ProductController Integration Tests")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String jwtToken;
    private Product testProduct;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        jpaProductRepository.deleteAll();
        jpaUserRepository.deleteAll();

        // Create test user and generate token
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(user);
        jwtToken = jwtService.generateToken("testuser");

        // Create test product
        testProduct = new Product(1L, "Test Product", 100.0);
        productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setPrice(100.0);
    }

    @Test
    @DisplayName("Should get all products successfully with authentication")
    void testGetAllProductsSuccess() throws Exception {
        // Arrange
        Product product1 = new Product(1L, "Product 1", 100.0);
        Product product2 = new Product(2L, "Product 2", 200.0);
        productRepository.save(product1);
        productRepository.save(product2);

        // Act & Assert
        mockMvc.perform(get("/api/products")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Berhasil mengambil data produk"))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name").value("Product 1"))
                .andExpect(jsonPath("$.data[1].name").value("Product 2"));
    }

    @Test
    @DisplayName("Should get all products without authentication - should fail")
    void testGetAllProductsUnauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should create product successfully")
    void testCreateProductSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/products")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Produk berhasil ditambahkan"))
                .andExpect(jsonPath("$.data.name").value("Test Product"))
                .andExpect(jsonPath("$.data.price").value(100.0));
    }

    @Test
    @DisplayName("Should fail create product without authentication")
    void testCreateProductUnauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should get product by id successfully")
    void testGetByIdSuccess() throws Exception {
        // Arrange
        Product saved = productRepository.save(testProduct);

        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", saved.getId())
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Berhasil mengambil data"))
                .andExpect(jsonPath("$.data.name").value("Test Product"))
                .andExpect(jsonPath("$.data.price").value(100.0));
    }

    @Test
    @DisplayName("Should return 404 when product not found by id")
    void testGetByIdNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", 999L)
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update product successfully")
    void testUpdateProductSuccess() throws Exception {
        // Arrange
        Product saved = productRepository.save(testProduct);
        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setName("Updated Product");
        updateRequest.setPrice(150.0);

        // Act & Assert
        mockMvc.perform(put("/api/products/{id}", saved.getId())
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Produk berhasil diperbarui"))
                .andExpect(jsonPath("$.data.name").value("Updated Product"))
                .andExpect(jsonPath("$.data.price").value(150.0));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent product")
    void testUpdateProductNotFound() throws Exception {
        // Arrange
        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setName("Updated Product");
        updateRequest.setPrice(150.0);

        // Act & Assert
        mockMvc.perform(put("/api/products/{id}", 999L)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete product successfully")
    void testDeleteProductSuccess() throws Exception {
        // Arrange
        Product saved = productRepository.save(testProduct);

        // Act & Assert
        mockMvc.perform(delete("/api/products/{id}", saved.getId())
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Produk berhasil dihapus"));
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent product")
    void testDeleteProductNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/products/{id}", 999L)
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should fail delete product without authentication")
    void testDeleteProductUnauthorized() throws Exception {
        // Arrange
        Product saved = productRepository.save(testProduct);

        // Act & Assert
        mockMvc.perform(delete("/api/products/{id}", saved.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void testGetAllProductsEmpty() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/products")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Berhasil mengambil data produk"))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @DisplayName("Should fail create product with invalid token")
    void testCreateProductInvalidToken() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/products")
                .header("Authorization", "Bearer invalidtoken123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isUnauthorized());
    }
}
