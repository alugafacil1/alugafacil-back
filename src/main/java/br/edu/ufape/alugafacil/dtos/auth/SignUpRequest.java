package br.edu.ufape.alugafacil.dtos.auth;

public record SignUpRequest(
    String name, 
    String email, 
    String phone, 
    String cpf, 
    String type,
    String password
) {}