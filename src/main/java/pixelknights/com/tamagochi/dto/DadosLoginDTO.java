package pixelknights.com.tamagochi.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosLoginDTO(
        @NotBlank String email,
        @NotBlank String senha
) {}
