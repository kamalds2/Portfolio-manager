package Managefolio.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
// ...existing code...

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {
	"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
	"spring.datasource.driver-class-name=org.h2.Driver",
	"spring.datasource.username=sa",
	"spring.datasource.password=",
	"spring.jpa.hibernate.ddl-auto=create-drop",
	"spring.jpa.defer-datasource-initialization=false",
	"spring.sql.init.mode=never",
	"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
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
