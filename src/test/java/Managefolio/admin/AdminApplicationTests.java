package Managefolio.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:mysql://localhost:3306/portfolio_test",
    "spring.datasource.username=root",
    "spring.datasource.password=testpass"
})
class AdminApplicationTests {

	@Test
	void contextLoads() {
		// This test verifies that the Spring context loads successfully
		// which ensures all your beans are configured correctly
	}

}
