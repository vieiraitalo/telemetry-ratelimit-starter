package com.example.starter.ratelimit;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
/**
 * Aspect that applies Resilience4j's rate limiting to all Spring REST controllers.
 * It wraps the controller method execution in a rate limiter.
 */

@Aspect
@Component
public class RateLimitAspect {

    private final RateLimiter rateLimiter;

    public RateLimitAspect(RateLimiterRegistry registry) {
        this.rateLimiter = registry.rateLimiter("default");
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object rateLimit(ProceedingJoinPoint pjp) throws Throwable {
        return RateLimiter.decorateCheckedSupplier(rateLimiter, pjp::proceed).get();
    }
}