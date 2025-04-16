# Telemetry Ratelimit Starter

This Spring Boot starter automatically integrates telemetry and rate limiting into your application.

## Features

### 1. Request Telemetry
- Captures request execution time
- Captures HTTP response status
- Conditionally captures response body
- Allows dynamic data insertion via `TelemetryContext`

### 2. Rate Limiting with Resilience4j
- Automatically applied to all `@RestController` endpoints
- Applied to Apache Camel routes via `RoutePolicy`

### 3. JWT Token Caching
- Locally caches the token with a dynamic expiration
- Subtracts a configurable value (`starter.jwt.expiry-offset-seconds`) from token's TTL

### 4. Apache Camel Support (Optional)
- Enabled via environment variable `STARTER_CAMEL_ENABLED=true`

## Default Configuration (application.yml)

```yaml
starter:
  telemetry:
    enabled: true
    capture-body: true
    capture-body-on-error: true
  ratelimit:
    enabled: true
  jwt:
    expiry-offset-seconds: 20
  camel:
    enabled: false
```

---

## Usage

Just add this starter as a dependency in your Spring Boot project:

```xml
<dependency>
  <groupId>com.example</groupId>
  <artifactId>telemetry-ratelimit-starter</artifactId>
  <version>1.0.0</version>
</dependency>
```

No additional configuration is needed 🎉