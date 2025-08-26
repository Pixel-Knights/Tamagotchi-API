package br.com.pixelknights.tamagochi.dto;

import br.com.pixelknights.tamagochi.Enum.TipoTamagochi;

public record TamagochiDTO(
        String nome,
        TipoTamagochi tipoTamagochi
) {}
