package br.edu.ufape.alugafacil.services;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.ufape.alugafacil.services.interfaces.IFileStorageService;

@Service
@Profile("dev")
public class LocalFileStorageService implements IFileStorageService {
	private final Path fileStorageLocation;
	
	public LocalFileStorageService() {
		this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
		
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao criar pasta uploads", e);
		}
 	}
	
	@Override
	public String uploadFile(MultipartFile file) {
		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
		try {
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			
			return ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/uploads/")
					.path(fileName)
					.toUriString();
		} catch (IOException e) {
			throw new RuntimeException("Erro ao salvar localmente", e);
		}	}

	@Override
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {

            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            Files.deleteIfExists(filePath);
            
            System.out.println("Arquivo deletado com sucesso: " + fileName);
        } catch (IOException e) {
            System.err.println("Não foi possível deletar o arquivo: " + fileUrl);
            e.printStackTrace();
        }
    }
	
	
}
