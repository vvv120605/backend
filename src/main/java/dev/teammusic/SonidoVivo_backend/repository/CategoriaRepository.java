package dev.teammusic.SonidoVivo_backend.repository;

import dev.teammusic.SonidoVivo_backend.model.Categoria;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombre(String nombre);
    
}
