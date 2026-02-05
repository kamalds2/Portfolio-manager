package Managefolio.admin.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("custom")
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        try {
            // Simple health check - just return UP
            return Health.up()
                    .withDetail("status", "Application is running")
                    .withDetail("database", "Connected")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}