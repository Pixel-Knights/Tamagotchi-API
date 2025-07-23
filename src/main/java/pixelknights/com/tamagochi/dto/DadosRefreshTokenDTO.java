package pixelknights.com.tamagochi.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosRefreshTokenDTO(
        @NotBlank String refreshToken
) {}
