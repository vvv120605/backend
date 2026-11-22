package dev.teammusic.SonidoVivo_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import dev.teammusic.SonidoVivo_backend.dto.BoletaRequestDTO;
import dev.teammusic.SonidoVivo_backend.dto.BoletaResponseDTO;
import dev.teammusic.SonidoVivo_backend.dto.DetalleBoletaRequestDTO;
import dev.teammusic.SonidoVivo_backend.model.Boleta;
import dev.teammusic.SonidoVivo_backend.model.DetalleBoleta;
import dev.teammusic.SonidoVivo_backend.model.EstadoBoleta;
import dev.teammusic.SonidoVivo_backend.model.Producto;
import dev.teammusic.SonidoVivo_backend.model.Usuario;
import dev.teammusic.SonidoVivo_backend.repository.BoletaRepository;
import dev.teammusic.SonidoVivo_backend.repository.ProductoRepository;
import dev.teammusic.SonidoVivo_backend.repository.UsuarioRepository;

// Pruebas unitarias de la lógica de compra: los repositorios son "mocks" (simulados),
// así que no se necesita base de datos.
@ExtendWith(MockitoExtension.class)
class BoletaServiceTest {

    @Mock
    private BoletaRepository boletaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private BoletaService service;

    private Usuario cliente;
    private Producto guitarra;

    @BeforeEach
    void prepararDatos() {
        cliente = new Usuario();
        cliente.setId(1L);
        cliente.setNombre("Cliente Prueba");
        cliente.setEmail("cliente@test.cl");

        guitarra = new Producto();
        guitarra.setId(10L);
        guitarra.setNombre("Fender Stratocaster");
        guitarra.setPrecio(1000);
        guitarra.setStock(5);
    }

    @Test
    void crearCalculaTotalYDescuentaStock() {
        when(usuarioRepository.findByEmail("cliente@test.cl")).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(guitarra));
        when(boletaRepository.save(any(Boleta.class))).thenAnswer(inv -> inv.getArgument(0));

        BoletaRequestDTO pedido = new BoletaRequestDTO(List.of(new DetalleBoletaRequestDTO(10L, 2)));
        BoletaResponseDTO boleta = service.crear(pedido, "cliente@test.cl");

        assertEquals(2000, boleta.total());
        assertEquals(1000, boleta.detalles().get(0).precioUnitario());
        assertEquals(EstadoBoleta.PENDIENTE, boleta.estado());
        assertEquals("Cliente Prueba", boleta.usuarioNombre());
        assertEquals(3, guitarra.getStock());
    }

    @Test
    void crearJuntaProductosRepetidosEnUnSoloDetalle() {
        when(usuarioRepository.findByEmail("cliente@test.cl")).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(guitarra));
        when(boletaRepository.save(any(Boleta.class))).thenAnswer(inv -> inv.getArgument(0));

        BoletaRequestDTO pedido = new BoletaRequestDTO(List.of(
                new DetalleBoletaRequestDTO(10L, 2),
                new DetalleBoletaRequestDTO(10L, 1)));
        BoletaResponseDTO boleta = service.crear(pedido, "cliente@test.cl");

        assertEquals(1, boleta.detalles().size());
        assertEquals(3, boleta.detalles().get(0).cantidad());
        assertEquals(3000, boleta.total());
        assertEquals(2, guitarra.getStock());
    }

    @Test
    void crearConStockInsuficienteLanza409YNoGuarda() {
        when(usuarioRepository.findByEmail("cliente@test.cl")).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(guitarra));

        BoletaRequestDTO pedido = new BoletaRequestDTO(List.of(new DetalleBoletaRequestDTO(10L, 50)));
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.crear(pedido, "cliente@test.cl"));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        assertEquals(5, guitarra.getStock());
        verify(boletaRepository, never()).save(any());
    }

    @Test
    void crearConProductoInexistenteLanza404() {
        when(usuarioRepository.findByEmail("cliente@test.cl")).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        BoletaRequestDTO pedido = new BoletaRequestDTO(List.of(new DetalleBoletaRequestDTO(99L, 1)));
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.crear(pedido, "cliente@test.cl"));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    void anularDevuelveElStock() {
        Boleta boleta = boletaConUnDetalle(2);
        guitarra.setStock(3);
        when(boletaRepository.findById(1L)).thenReturn(Optional.of(boleta));
        when(boletaRepository.save(any(Boleta.class))).thenAnswer(inv -> inv.getArgument(0));

        BoletaResponseDTO resultado = service.cambiarEstado(1L, EstadoBoleta.ANULADA);

        assertEquals(EstadoBoleta.ANULADA, resultado.estado());
        assertEquals(5, guitarra.getStock());
    }

    @Test
    void unaBoletaAnuladaNoPuedeCambiarDeEstado() {
        Boleta boleta = boletaConUnDetalle(2);
        boleta.setEstado(EstadoBoleta.ANULADA);
        when(boletaRepository.findById(1L)).thenReturn(Optional.of(boleta));

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.cambiarEstado(1L, EstadoBoleta.PAGADA));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        assertEquals(EstadoBoleta.ANULADA, boleta.getEstado());
    }

    // Arma una boleta guardada con un detalle de la guitarra
    private Boleta boletaConUnDetalle(int cantidad) {
        Boleta boleta = new Boleta();
        boleta.setId(1L);
        boleta.setUsuario(cliente);
        boleta.setTotal(cantidad * 1000);

        DetalleBoleta detalle = new DetalleBoleta();
        detalle.setProducto(guitarra);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(1000);
        detalle.setSubtotal(cantidad * 1000);
        boleta.agregarDetalle(detalle);
        return boleta;
    }
}
