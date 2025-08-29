package com.mahmoud.thoth;

import com.mahmoud.thoth.config.TestcontainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TestcontainersSmokeTest extends TestcontainersConfig {

    @Test
    void contextLoads() {
        // This test verifies that:
        // 1. The PostgreSQL container starts successfully
        // 2. The application context loads with the containerized database
        // 3. Flyway migrations run successfully
        // 4. All beans are created properly
    }
}
