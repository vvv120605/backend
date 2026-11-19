package dev.teammusic.SonidoVivo_backend.repository;

import dev.teammusic.SonidoVivo_backend.model.DetalleBoleta;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, Long> {

    List<DetalleBoleta> findByBoletaId(Long boletaId);
    
}
