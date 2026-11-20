package dev.teammusic.SonidoVivo_backend.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BoletaRequestDTO(
        @NotNull Long usuarioId,
        @NotEmpty @Valid List<DetalleBoletaRequestDTO> detalles) {
}