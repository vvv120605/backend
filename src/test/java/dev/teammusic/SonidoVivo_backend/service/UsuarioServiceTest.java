package dev.teammusic.SonidoVivo_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import dev.teammusic.SonidoVivo_backend.model.Rol;
import dev.teammusic.SonidoVivo_backend.model.Usuario;
import dev.teammusic.SonidoVivo_backend.repository.BoletaRepository;
import dev.teammusic.SonidoVivo_backend.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;
    @Mock
    private BoletaRepository boletaRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    @Test
    void registrarSiempreCreaClienteYEncriptaLaClave() {
        Usuario nuevo = new Usuario();
        nuevo.setNombre("Intruso");
        nuevo.setEmail("intruso@test.cl");
        nuevo.setPassword("1234");
        nuevo.setRol(Rol.ADMIN); // intenta registrarse como administrador

        when(repository.findByEmail("intruso@test.cl")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("clave-encriptada");
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario guardado = service.crear(nuevo);

        assertEquals(Rol.CLIENTE, guardado.getRol());
        assertEquals("clave-encriptada", guardado.getPassword());
    }

    @Test
    void registrarConEmailRepetidoLanza409() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setEmail("repetido@test.cl");

        Usuario nuevo = new Usuario();
        nuevo.setEmail("repetido@test.cl");
        nuevo.setPassword("1234");

        when(repository.findByEmail("repetido@test.cl")).thenReturn(Optional.of(existente));

        ResponseStatusException error = assertThrows(ResponseStatusException.class, () -> service.crear(nuevo));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(repository, never()).save(any());
    }

    @Test
        void registrarConRunRepetidoLanza409() {
            Usuario existente = new Usuario();
            existente.setId(1L);
            existente.setRun("12345678-K");

            Usuario nuevo = new Usuario();
            nuevo.setEmail("nuevo@test.cl");
            nuevo.setRun("12.345.678-k"); // mismo RUN escrito con puntos y k minúscula
            nuevo.setPassword("1234");

            when(repository.findByEmail("nuevo@test.cl")).thenReturn(Optional.empty());
            when(repository.findByRun("12345678-K")).thenReturn(Optional.of(existente));

            ResponseStatusException error = assertThrows(ResponseStatusException.class, () -> service.crear(nuevo));

            assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
            verify(repository, never()).save(any());
        }
}