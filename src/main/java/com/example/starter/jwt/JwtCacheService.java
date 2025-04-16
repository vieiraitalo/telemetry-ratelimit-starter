package com.example.starter.jwt;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
/**
 * A service that caches JWT tokens using Caffeine with a dynamic TTL,
 * calculated by subtracting a configurable offset from the token's actual expiration time.
 */

@Service
public class JwtCacheService {

    private final Cache<String, String> jwtCache;

    @Value("${starter.jwt.expiry-offset-seconds:20}")
    private int expiryOffsetSeconds;

    public JwtCacheService() {
        this.jwtCache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }

    public String getToken(String cacheKey, JwtSupplier supplier) {
        return jwtCache.get(cacheKey, key -> {
            JwtToken jwt = supplier.get();
            jwtCache.policy().expireAfterWrite().ifPresent(policy ->
                    policy.setExpiresAfter(jwt.getExpiresIn() - expiryOffsetSeconds, TimeUnit.SECONDS));
            return jwt.getToken();
        });
    }

    public interface JwtSupplier {
        JwtToken get();
    }

    public static class JwtToken {
        private final String token;
        private final long expiresIn;

        public JwtToken(String token, long expiresIn) {
            this.token = token;
            this.expiresIn = expiresIn;
        }

        public String getToken() {
            return token;
        }

        public long getExpiresIn() {
            return expiresIn;
        }
    }
}