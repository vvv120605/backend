package dev.teammusic.SonidoVivo_backend.service;

import dev.teammusic.SonidoVivo_backend.model.Producto;
import dev.teammusic.SonidoVivo_backend.repository.CategoriaRepository;
import dev.teammusic.SonidoVivo_backend.repository.ProductoRepository;


import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {
 
    private final ProductoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        return repository.findByCategoriaId(categoriaId);
    }

    public Optional<Producto> crear(Producto producto) {
        return categoriaRepository.findById(producto.getCategoria().getId())
                .map(categoria -> {
                    producto.setId(null);
                    producto.setCategoria(categoria);
                    return repository.save(producto);
                });
    }

    public Optional<Producto> actualizar(Long id, Producto datos) {
        return repository.findById(id)
                .flatMap(existente -> 
                    categoriaRepository.findById(datos.getCategoria().getId())
                        .map(categoria -> {
                            existente.setCodigo(datos.getCodigo());
                            existente.setNombre(datos.getNombre());
                            existente.setDescripcion(datos.getDescripcion());
                            existente.setPrecio(datos.getPrecio());
                            existente.setStock(datos.getStock());
                            existente.setStockCritico(datos.getStockCritico());
                            existente.setImagen(datos.getImagen());
                            existente.setCategoria(categoria);
                            return repository.save(existente);
                }));
    }

   public boolean eliminar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}