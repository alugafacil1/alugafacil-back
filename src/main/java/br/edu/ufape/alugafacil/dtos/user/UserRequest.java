package br.edu.ufape.alugafacil.dtos.user;

import br.edu.ufape.alugafacil.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest (
        
        @NotBlank(message = "O nome é obrigatório")
        String name,
        
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "O formato do e-mail é inválido")
        String email,
        
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 4, message = "A senha deve ter no mínimo 6 caracteres")
        String password,
        
        @NotBlank(message = "O CPF é obrigatório")
        String cpf,
        
        @NotBlank(message = "O telefone é obrigatório")
        String phoneNumber,
        
        @NotNull(message = "É obrigatório selecionar o tipo de usuário")
        UserType userType,

        String photoUrl,
        String creciNumber,
        String fcmToken
        
) {}