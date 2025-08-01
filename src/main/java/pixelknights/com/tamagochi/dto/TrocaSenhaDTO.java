package pixelknights.com.tamagochi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record TrocaSenhaDTO(
        @Valid @NotBlank String senhaAntiga,
        @Valid @NotBlank String senhaNova
) { }
