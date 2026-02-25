package br.edu.ufape.alugafacil.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RealtorRegistrationRequest(

    @NotBlank(message = "O nome do corretor é obrigatório")
    String name,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O formato do e-mail é inválido")
    String email,

    @NotBlank(message = "O CPF é obrigatório")
    String cpf,

    @NotBlank(message = "O número do CRECI é obrigatório")
    String creciNumber,

    @NotBlank(message = "O telefone de contato é obrigatório")
    String phoneNumber,

    @NotBlank(message = "A senha de primeiro acesso é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String password
    
) {}