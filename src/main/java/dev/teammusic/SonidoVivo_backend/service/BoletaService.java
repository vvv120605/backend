package dev.teammusic.SonidoVivo_backend.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dev.teammusic.SonidoVivo_backend.dto.BoletaRequestDTO;
import dev.teammusic.SonidoVivo_backend.dto.BoletaResponseDTO;
import dev.teammusic.SonidoVivo_backend.dto.DetalleBoletaResponseDTO;
import dev.teammusic.SonidoVivo_backend.model.Boleta;
import dev.teammusic.SonidoVivo_backend.model.DetalleBoleta;
import dev.teammusic.SonidoVivo_backend.model.EstadoBoleta;
import dev.teammusic.SonidoVivo_backend.model.Producto;
import dev.teammusic.SonidoVivo_backend.model.Usuario;
import dev.teammusic.SonidoVivo_backend.repository.BoletaRepository;
import dev.teammusic.SonidoVivo_backend.repository.ProductoRepository;
import dev.teammusic.SonidoVivo_backend.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BoletaService {

    private final BoletaRepository boletaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public BoletaService(BoletaRepository boletaRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository) {
        this.boletaRepository = boletaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    // Listar todas las boletas
    public List<BoletaResponseDTO> listarTodas() {
        return boletaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // Obtener por ID
    public BoletaResponseDTO obtenerPorId(Long id) {
        return toResponse(buscarBoleta(id));
    }

    // Listar por usuario
    public List<BoletaResponseDTO> listarPorUsuario(Long usuarioId) {
        return boletaRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toResponse)
                .toList();
    }

    // Listar por estado
    public List<BoletaResponseDTO> listarPorEstado(EstadoBoleta estado) {
        return boletaRepository.findByEstado(estado).stream()
                .map(this::toResponse)
                .toList();
    }

    // Crear boleta: calcula el total, valida y descuenta el stock
    public BoletaResponseDTO crear(BoletaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Boleta boleta = new Boleta();
        boleta.setUsuario(usuario);

        // Si el mismo producto viene repetido, se suman las cantidades
        // (la tabla no permite el mismo producto dos veces en una boleta)
        Map<Long, Integer> cantidades = new LinkedHashMap<>();
        dto.detalles().forEach(item -> cantidades.merge(item.productoId(), item.cantidad(), Integer::sum));

        int total = 0;
        for (Map.Entry<Long, Integer> item : cantidades.entrySet()) {
            Producto producto = productoRepository.findById(item.getKey())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Producto no encontrado: " + item.getKey()));

            int cantidad = item.getValue();
            if (producto.getStock() < cantidad) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Stock insuficiente para: " + producto.getNombre());
            }
            producto.setStock(producto.getStock() - cantidad);

            int subtotal = producto.getPrecio() * cantidad;

            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(subtotal);
            boleta.agregarDetalle(detalle);

            total += subtotal;
        }

        boleta.setTotal(total);
        return toResponse(boletaRepository.save(boleta));
    }

    // Cambiar el estado de la boleta
    public BoletaResponseDTO cambiarEstado(Long id, EstadoBoleta estado) {
        Boleta boleta = buscarBoleta(id);
        boleta.setEstado(estado);
        return toResponse(boletaRepository.save(boleta));
    }

    // Métodos auxiliares

    private Boleta buscarBoleta(Long id) {
        return boletaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Boleta no encontrada"));
    }

    private BoletaResponseDTO toResponse(Boleta boleta) {
        List<DetalleBoletaResponseDTO> detalles = boleta.getDetalles().stream()
                .map(d -> new DetalleBoletaResponseDTO(
                        d.getId(),
                        d.getProducto().getId(),
                        d.getProducto().getNombre(),
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        d.getSubtotal()))
                .toList();

        return new BoletaResponseDTO(
                boleta.getId(),
                boleta.getFecha(),
                boleta.getTotal(),
                boleta.getEstado(),
                boleta.getUsuario().getId(),
                boleta.getUsuario().getNombre(),
                detalles);
    }
}