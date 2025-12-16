package br.edu.ufape.alugafacil.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {
	String uploadFile(MultipartFile file);
	void deleteFile(String fileUrl);
}
