package com.example.starter;

import com.example.starter.filter.TelemetryFilter;
import com.example.starter.telemetry.TelemetryContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TelemetryFilterTest {

    private TelemetryFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        TelemetryContext.setEnvironment(new org.springframework.mock.env.MockEnvironment());
        filter = new TelemetryFilter();
        request = mock(HttpServletRequest.class);
        response = new MockHttpServletResponse();
        chain = mock(FilterChain.class);
    }

    @Test
    void testFilterExecution() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/test");

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(any(), any());
    }
}