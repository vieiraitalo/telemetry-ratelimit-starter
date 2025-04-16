package com.example.starter;

import com.example.starter.jwt.JwtCacheService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtCacheServiceTest {

    @Test
    void testTokenIsCached() {
        JwtCacheService service = new JwtCacheService();
        String token = service.getToken("user", () -> new JwtCacheService.JwtToken("abc123", 60));
        assertEquals("abc123", token);

        String tokenCached = service.getToken("user", () -> new JwtCacheService.JwtToken("shouldNotBeUsed", 60));
        assertEquals("abc123", tokenCached);
    }
}