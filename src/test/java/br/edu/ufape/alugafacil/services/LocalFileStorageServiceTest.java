package br.edu.ufape.alugafacil.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class LocalFileStorageServiceTest {

    private LocalFileStorageService service;
    
    private final Path uploadDir = Path.of("uploads");

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("http");
        request.setServerName("localhost");
        request.setServerPort(8080);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        service = new LocalFileStorageService();
    }

    @AfterEach
    void tearDown() throws IOException {
        FileSystemUtils.deleteRecursively(uploadDir);
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void shouldUploadFileLocallySuccessfully() {
        MockMultipartFile file = new MockMultipartFile(
            "file", "teste.txt", "text/plain", "Hello World".getBytes()
        );

        String fileUrl = service.uploadFile(file);

        assertTrue(fileUrl.startsWith("http://localhost:8080/uploads/"));
        assertTrue(fileUrl.contains("teste.txt"));

        String generatedFileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        Path filePath = uploadDir.resolve(generatedFileName);
        
        assertTrue(Files.exists(filePath), "O arquivo deveria existir na pasta uploads");
    }
}