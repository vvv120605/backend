package dev.teammusic.SonidoVivo_backend.repository;

import dev.teammusic.SonidoVivo_backend.model.Producto;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Optional<Producto> findByCodigo(String codigo);

    List<Producto> findByCategoriaId(Long categoriaId);
    
    boolean existsByCategoriaId(Long categoriaId);

}
