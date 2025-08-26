package br.com.pixelknights.tamagochi.dto;

public record DadosTokenDTO (
        String tokenJWT,
        String refreshToken
){}
