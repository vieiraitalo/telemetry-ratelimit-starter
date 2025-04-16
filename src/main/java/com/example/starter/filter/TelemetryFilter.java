package com.example.starter.filter;

import com.example.starter.telemetry.TelemetryContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
/**
 * A servlet filter that captures telemetry data such as execution time,
 * HTTP status code, and optionally the response body. This data is passed
 * to the TelemetryContext for logging or further processing.
 */

public class TelemetryFilter extends OncePerRequestFilter {

    private final boolean captureBody;
    private final boolean onlyCaptureError;

    public TelemetryFilter() {
        Environment env = TelemetryContext.getEnvironment();
        this.captureBody = Boolean.parseBoolean(env.getProperty("starter.telemetry.capture-body", "true"));
        this.onlyCaptureError = Boolean.parseBoolean(env.getProperty("starter.telemetry.capture-body-on-error", "true"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(request, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - start;
            int status = wrappedResponse.getStatus();
            String body = null;

            if (captureBody && (!onlyCaptureError || status != 200)) {
                byte[] content = wrappedResponse.getContentAsByteArray();
                body = new String(content, StandardCharsets.UTF_8);
            }

            TelemetryContext.logTelemetry(request.getMethod(), request.getRequestURI(), status, duration, body);
            wrappedResponse.copyBodyToResponse();
        }
    }
}