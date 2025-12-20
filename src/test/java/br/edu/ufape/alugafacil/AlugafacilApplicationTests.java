package br.edu.ufape.alugafacil;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import br.edu.ufape.alugafacil.config.TestConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class AlugafacilApplicationTests {

	@Test
	void contextLoads() {
	}

}
