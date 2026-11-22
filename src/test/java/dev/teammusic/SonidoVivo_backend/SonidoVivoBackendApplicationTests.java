package dev.teammusic.SonidoVivo_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Levanta la aplicación completa con una base H2 en memoria (modo MySQL):
// comprueba que Flyway aplica las migraciones y que las entidades calzan con las tablas.
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect"
})
class SonidoVivoBackendApplicationTests {

	@Test
	void contextLoads() {
	}

}