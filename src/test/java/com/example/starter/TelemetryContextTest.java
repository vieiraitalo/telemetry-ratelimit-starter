package com.example.starter;

import com.example.starter.telemetry.TelemetryContext;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TelemetryContextTest {

    @Test
    void testPutAndGetAll() {
        TelemetryContext.put("key", "value");
        Map<String, Object> data = TelemetryContext.getAll();
        assertEquals("value", data.get("key"));
    }

    @Test
    void testClear() {
        TelemetryContext.put("key", "value");
        TelemetryContext.clear();
        assertTrue(TelemetryContext.getAll().isEmpty());
    }

    @Test
    void testSetEnvironment() {
        MockEnvironment env = new MockEnvironment();
        TelemetryContext.setEnvironment(env);
        assertNotNull(TelemetryContext.getEnvironment());
    }

    @Test
    void testLogTelemetry() {
        TelemetryContext.put("custom", "data");
        TelemetryContext.logTelemetry("GET", "/uri", 200, 100L, "body");
    }
}