package br.edu.ufape.alugafacil.dtos.auth;

public record LoginRequest(
    String email, 
    String password
) {}