package dev.teammusic.SonidoVivo_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import dev.teammusic.SonidoVivo_backend.model.EstadoBoleta;

public record BoletaResponseDTO(
        Long id,
        LocalDateTime fecha,
        Integer total,
        EstadoBoleta estado,
        Long usuarioId,
        String usuarioNombre,
        List<DetalleBoletaResponseDTO> detalles) {
}