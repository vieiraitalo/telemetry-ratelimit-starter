package com.example.starter.ratelimit;

import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.spi.RoutePolicySupport;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import java.util.concurrent.Callable;

/**
 * This class defines a Camel RoutePolicy that applies a rate limiter
 * to any route it is attached to, using Resilience4j.
 */
public class TelemetryRoutePolicy extends RoutePolicySupport {

    private final RateLimiter rateLimiter;

    public TelemetryRoutePolicy() {
        // Initialize a rate limiter from the default registry
        this.rateLimiter = RateLimiterRegistry.ofDefaults().rateLimiter("camel-default");
    }

    /**
     * Called before a Camel route processes an exchange.
     * Applies rate limiting logic and blocks the route if the limit is exceeded.
     */
    @Override
    public void onExchangeBegin(Route route, Exchange exchange) {
        try {
            // Wrap a dummy callable to enforce rate limit
            RateLimiter.decorateCallable(rateLimiter, (Callable<Void>) () -> null).call();
        } catch (Exception e) {
            // If the rate limit is exceeded, set an exception on the exchange
            exchange.setException(new RuntimeException("Rate limit exceeded for route: " + route.getId()));
        }
    }
}