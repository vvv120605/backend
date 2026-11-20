package dev.teammusic.SonidoVivo_backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetalleBoletaRequestDTO(
        @NotNull Long productoId,
        @NotNull @Positive Integer cantidad) {
}
