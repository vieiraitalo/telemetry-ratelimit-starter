package com.example.starter;

import com.example.starter.ratelimit.RateLimitAspect;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RateLimitAspectTest {

    private ProceedingJoinPoint pjp;
    private RateLimitAspect aspect;

    @BeforeEach
    void setup() throws Throwable {
        RateLimiterRegistry registry = RateLimiterRegistry.ofDefaults();
        aspect = new RateLimitAspect(registry);

        pjp = mock(ProceedingJoinPoint.class);
        when(pjp.proceed()).thenReturn("success");
    }

    @Test
    void testRateLimitAspect() throws Throwable {
        Object result = aspect.rateLimit(pjp);
        assertEquals("success", result);
        verify(pjp).proceed();
    }
}