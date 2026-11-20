package dev.teammusic.SonidoVivo_backend.repository;

import dev.teammusic.SonidoVivo_backend.model.Boleta;
import dev.teammusic.SonidoVivo_backend.model.EstadoBoleta;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {

    List<Boleta> findByUsuarioId(Long usuarioId);

    List<Boleta> findByEstado(EstadoBoleta estado);

    boolean existsByUsuarioId(Long usuarioId);
}