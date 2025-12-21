package br.edu.ufape.alugafacil.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.google.firebase.FirebaseApp;

import br.edu.ufape.alugafacil.services.interfaces.IFileStorageService;

@TestConfiguration
@Profile("test")
public class TestConfig {

    @Bean
    @Primary
    public FirebaseApp firebaseApp() {
        return Mockito.mock(FirebaseApp.class);
    }
    
    @Bean
    @Primary
    public IFileStorageService fileStorageService() {
        return Mockito.mock(IFileStorageService.class);
    }
}