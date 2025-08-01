package pixelknights.com.tamagochi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record UsuarioCriacaoDTO (
        @Valid @NotBlank String userName,
        @Valid @NotBlank String email,
        @Valid @NotBlank String senha
) {}
