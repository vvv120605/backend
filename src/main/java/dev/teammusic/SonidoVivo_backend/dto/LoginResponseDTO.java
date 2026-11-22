package dev.teammusic.SonidoVivo_backend.dto;

public record LoginResponseDTO(String token, String refreshToken, String rol, String nombre) {}