package br.edu.ufape.alugafacil.dtos;

import br.edu.ufape.alugafacil.enums.UserType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanRequestDTO {

    @NotBlank(message = "O nome do plano é obrigatório")
    private String name;

    @NotNull(message = "O preço é obrigatório")
    @Min(value = 0, message = "O preço deve ser maior ou igual a zero")
    private Integer priceInCents;

    @NotNull(message = "O campo hasVideo é obrigatório")
    private Boolean hasVideo;

    @NotNull(message = "A quantidade de imagens é obrigatória")
    @Min(value = 0, message = "A quantidade de imagens deve ser maior ou igual a zero")
    private Integer imagesCount;

    @NotNull(message = "A quantidade de propriedades é obrigatória")
    @Min(value = 0, message = "A quantidade de propriedades deve ser maior ou igual a zero")
    private Integer propertiesCount;

    @NotNull(message = "O campo isPriority é obrigatório")
    private Boolean isPriority;

    @NotNull(message = "O campo hasNotification é obrigatório")
    private Boolean hasNotification;

    @NotNull(message = "O público alvo (targetAudience) é obrigatório")
    private UserType targetAudience;
}
