package Managefolio.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import Managefolio.admin.config.TestSecurityConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create",
    "spring.jpa.defer-datasource-initialization=true",
    "logging.level.org.hibernate.SQL=ERROR",
    "logging.level.org.springframework.security=ERROR"
})
class AdminApplicationTests {

	@Test
	void contextLoads() {
		// This test verifies that the Spring context loads successfully
		// which ensures all your beans are configured correctly
	}

}
