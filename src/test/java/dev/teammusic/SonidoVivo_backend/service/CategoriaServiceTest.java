package dev.teammusic.SonidoVivo_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import dev.teammusic.SonidoVivo_backend.model.Categoria;
import dev.teammusic.SonidoVivo_backend.repository.CategoriaRepository;
import dev.teammusic.SonidoVivo_backend.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repository;
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CategoriaService service;

    @Test
    void crearConNombreRepetidoLanza409() {
        Categoria existente = new Categoria();
        existente.setId(1L);
        existente.setNombre("Guitarras");

        Categoria nueva = new Categoria();
        nueva.setNombre("Guitarras");

        when(repository.findByNombre("Guitarras")).thenReturn(Optional.of(existente));

        ResponseStatusException error = assertThrows(ResponseStatusException.class, () -> service.crear(nueva));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(repository, never()).save(any());
    }

    @Test
    void eliminarCategoriaConProductosLanza409() {
        when(repository.existsById(1L)).thenReturn(true);
        when(productoRepository.existsByCategoriaId(1L)).thenReturn(true);

        ResponseStatusException error = assertThrows(ResponseStatusException.class, () -> service.eliminar(1L));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(repository, never()).deleteById(anyLong());
    }
}