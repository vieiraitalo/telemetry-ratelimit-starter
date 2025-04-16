package com.example.starter;

import com.example.starter.ratelimit.TelemetryRoutePolicy;
import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class TelemetryRoutePolicyTest {

    private TelemetryRoutePolicy policy;
    private Route route;
    private Exchange exchange;

    @BeforeEach
    void setup() {
        policy = new TelemetryRoutePolicy();
        route = mock(Route.class);
        exchange = mock(Exchange.class);
        when(route.getId()).thenReturn("route-1");
    }

    @Test
    void testRateLimitingPass() {
        policy.onExchangeBegin(route, exchange);
        verify(exchange, never()).setException(any());
    }
}