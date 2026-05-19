package com.example.demo.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtService Unit Tests")
class JwtServiceTest {

    private JwtService jwtService;
    private String jwtSecret = "mysecretkeyformachinelearningusedinexamplesonly12345";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(jwtSecret);
    }

    @Test
    @DisplayName("Should generate valid token")
    void testGenerateTokenSuccess() {
        // Act
        String token = jwtService.generateToken("testuser");

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    @DisplayName("Should extract correct username from token")
    void testExtractUsernameSuccess() {
        // Arrange
        String token = jwtService.generateToken("testuser");

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("testuser", username);
    }

    @Test
    @DisplayName("Should validate token successfully")
    void testIsTokenValidTrue() {
        // Arrange
        String token = jwtService.generateToken("testuser");

        // Act
        boolean isValid = jwtService.isTokenValid(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false for invalid token")
    void testIsTokenValidFalse() {
        // Act
        boolean isValid = jwtService.isTokenValid("invalidtoken123");

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should validate different usernames generate different tokens")
    void testDifferentUsernameDifferentToken() {
        // Arrange
        String token1 = jwtService.generateToken("user1");
        String token2 = jwtService.generateToken("user2");

        // Assert
        assertNotEquals(token1, token2);
        assertEquals("user1", jwtService.extractUsername(token1));
        assertEquals("user2", jwtService.extractUsername(token2));
    }

    @Test
    @DisplayName("Should return false for tampered token")
    void testTamperedTokenInvalid() {
        // Arrange
        String token = jwtService.generateToken("testuser");
        String tamperedToken = token.substring(0, token.length() - 5) + "tampered";

        // Act
        boolean isValid = jwtService.isTokenValid(tamperedToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should throw exception for blank jwt secret")
    void testBlankJwtSecretHandling() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new JwtService("");
        });
    }

    @Test
    @DisplayName("Should throw exception for null jwt secret")
    void testNullJwtSecretHandling() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new JwtService(null);
        });
    }

    @Test
    @DisplayName("Should maintain token consistency within same instance")
    void testTokenConsistency() {
        // Arrange
        String username = "consistencytest";
        String token = jwtService.generateToken(username);

        // Act & Assert - Multiple validations
        assertTrue(jwtService.isTokenValid(token));
        assertEquals(username, jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token));
    }
}
