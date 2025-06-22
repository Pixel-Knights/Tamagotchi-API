package pixelknights.com.tamagochi.dto;

import pixelknights.com.tamagochi.Enum.TipoTamagochi;

public record TamagochiDTO(
        String nome,
        TipoTamagochi tipoTamagochi
) {}
