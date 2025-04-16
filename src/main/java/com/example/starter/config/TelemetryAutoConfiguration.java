package com.example.starter.config;

import com.example.starter.filter.TelemetryFilter;
import com.example.starter.jwt.JwtCacheService;
import com.example.starter.ratelimit.RateLimitAspect;
import com.example.starter.ratelimit.TelemetryRoutePolicy;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.annotation.PostConstruct;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
/**
 * Auto-configuration class that registers beans for telemetry, rate limiting, and JWT caching.
 * Also applies route policies to Apache Camel routes if enabled via environment variable.
 */

@Configuration
public class TelemetryAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "starter.telemetry.enabled", havingValue = "true", matchIfMissing = true)
    public OncePerRequestFilter telemetryFilter() {
        return new TelemetryFilter();
    }

    @Bean
    @ConditionalOnProperty(name = "starter.ratelimit.enabled", havingValue = "true", matchIfMissing = true)
    public RateLimitAspect rateLimitAspect(RateLimiterRegistry registry) {
        return new RateLimitAspect(registry);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtCacheService jwtCacheService() {
        return new JwtCacheService();
    }

    @Autowired(required = false)
    private CamelContext camelContext;

    @PostConstruct
    public void initCamelSupport() {
        boolean camelEnabled = Boolean.parseBoolean(System.getenv().getOrDefault("STARTER_CAMEL_ENABLED", "false"));
        if (camelEnabled && camelContext != null) {
            camelContext.getRouteDefinitions().forEach(route ->
                route.routePolicy(new TelemetryRoutePolicy())
            );
        }
    }
}