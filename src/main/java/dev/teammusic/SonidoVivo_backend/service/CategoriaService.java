package dev.teammusic.SonidoVivo_backend.service;

import dev.teammusic.SonidoVivo_backend.model.Categoria;
import dev.teammusic.SonidoVivo_backend.repository.CategoriaRepository;
import dev.teammusic.SonidoVivo_backend.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;
    private final ProductoRepository productoRepository;

    public CategoriaService(CategoriaRepository repository, ProductoRepository productoRepository) {
        this.repository = repository;
        this.productoRepository = productoRepository;
    }

    public List<Categoria> listar() {
        return repository.findAll();
    }

    public Optional<Categoria> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Categoria crear(Categoria categoria) {
        validarNombreLibre(categoria.getNombre(), null);
        categoria.setId(null);
        return repository.save(categoria);
    }

    public Optional<Categoria> actualizar(Long id, Categoria categoria) {
        return repository.findById(id)
                .map(existingCategoria -> {
                    validarNombreLibre(categoria.getNombre(), id);
                    existingCategoria.setNombre(categoria.getNombre());
                    return repository.save(existingCategoria);
                });
    }

    public boolean eliminar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        // No se puede borrar una categoría que todavía tiene productos
        if (productoRepository.existsByCategoriaId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar una categoría que tiene productos");
        }
        repository.deleteById(id);
        return true;
    }

    // Verifica que el nombre no lo use otra categoría (idActual = la que se edita, o null al crear)
    private void validarNombreLibre(String nombre, Long idActual) {
        repository.findByNombre(nombre)
                .filter(c -> !c.getId().equals(idActual))
                .ifPresent(c -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Ya existe una categoría con ese nombre");
                });
    }
}