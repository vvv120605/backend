package dev.teammusic.SonidoVivo_backend.dto;

public record DetalleBoletaResponseDTO(
        Long id,
        Long productoId,
        String productoNombre,
        Integer cantidad,
        Integer precioUnitario,
        Integer subtotal) {
}
