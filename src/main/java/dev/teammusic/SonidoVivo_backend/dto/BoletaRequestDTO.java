package dev.teammusic.SonidoVivo_backend.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record BoletaRequestDTO(
        @NotEmpty @Valid List<DetalleBoletaRequestDTO> detalles) {
}