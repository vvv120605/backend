package dev.teammusic.SonidoVivo_backend.service;

import dev.teammusic.SonidoVivo_backend.model.Usuario;
import dev.teammusic.SonidoVivo_backend.repository.BoletaRepository;
import dev.teammusic.SonidoVivo_backend.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final BoletaRepository boletaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository,
                          BoletaRepository boletaRepository,
                          PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.boletaRepository = boletaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Usuario crear(Usuario usuario) {
        validarEmailLibre(usuario.getEmail(), null);
        usuario.setId(null);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    public Optional<Usuario> actualizar(Long id, Usuario datos) {
        return repository.findById(id)
                .map(existente -> {
                    validarEmailLibre(datos.getEmail(), id);
                    existente.setNombre(datos.getNombre());
                    existente.setEmail(datos.getEmail());
                    existente.setPassword(passwordEncoder.encode(datos.getPassword()));
                    existente.setRol(datos.getRol());
                    return repository.save(existente);
                });
    }

    public boolean eliminar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        if (boletaRepository.existsByUsuarioId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar un usuario que tiene boletas");
        }
        repository.deleteById(id);
        return true;
    }

    // Verifica que el email no lo use otro usuario (idActual = id del usuario que se edita, o null al crear)
    private void validarEmailLibre(String email, Long idActual) {
        repository.findByEmail(email)
                .filter(u -> !u.getId().equals(idActual))
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese email");
                });
    }
}