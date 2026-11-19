package dev.teammusic.SonidoVivo_backend.repository;

import dev.teammusic.SonidoVivo_backend.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
