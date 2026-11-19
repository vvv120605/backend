package dev.teammusic.SonidoVivo_backend.service;

import dev.teammusic.SonidoVivo_backend.model.Categoria;
import dev.teammusic.SonidoVivo_backend.repository.CategoriaRepository;


import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;



@Service
public class CategoriaService {
    
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> listar() {
        return repository.findAll();
    }

    public Optional<Categoria> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Categoria crear(Categoria categoria) {
        categoria.setId(null);
        return repository.save(categoria);
    }

    public Optional<Categoria> actualizar(Long id, Categoria categoria) {
        return repository.findById(id)
                .map(existingCategoria -> {
                    existingCategoria.setNombre(categoria.getNombre());
                    return repository.save(existingCategoria);
                });
    }

   public boolean eliminar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
