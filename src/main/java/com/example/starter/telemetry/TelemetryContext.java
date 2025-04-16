package com.example.starter.telemetry;

import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
/**
 * Thread-local context used to store and retrieve telemetry information during the lifecycle of a request.
 * It allows dynamic insertion and retrieval of key-value telemetry data and handles cleanup after logging.
 */

@Component
public class TelemetryContext {
    private static final ThreadLocal<Map<String, Object>> data = ThreadLocal.withInitial(HashMap::new);
    private static Environment environment;

    public static void put(String key, Object value) {
        data.get().put(key, value);
    }

    public static Map<String, Object> getAll() {
        return new HashMap<>(data.get());
    }

    public static void clear() {
        data.remove();
    }

    public static void logTelemetry(String method, String uri, int status, long duration, String body) {
        Map<String, Object> telemetry = getAll();
        telemetry.put("method", method);
        telemetry.put("uri", uri);
        telemetry.put("status", status);
        telemetry.put("duration", duration);
        telemetry.put("body", body);
        System.out.println("[TELEMETRY] " + telemetry);
        clear();
    }

    public static void setEnvironment(Environment env) {
        environment = env;
    }

    public static Environment getEnvironment() {
        return environment;
    }
}