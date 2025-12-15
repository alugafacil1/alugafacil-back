package br.edu.ufape.alugafacil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AlugafacilApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlugafacilApplication.class, args);
	}

}
