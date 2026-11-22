package dev.teammusic.SonidoVivo_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import dev.teammusic.SonidoVivo_backend.dto.BoletaRequestDTO;
import dev.teammusic.SonidoVivo_backend.dto.BoletaResponseDTO;
import dev.teammusic.SonidoVivo_backend.model.EstadoBoleta;
import dev.teammusic.SonidoVivo_backend.service.BoletaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/boletas")
public class BoletaController {

    private final BoletaService service;

    public BoletaController(BoletaService service) {
        this.service = service;
    }

    @GetMapping
    public List<BoletaResponseDTO> listar() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public BoletaResponseDTO obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<BoletaResponseDTO> listarPorUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @GetMapping("/estado/{estado}")
    public List<BoletaResponseDTO> listarPorEstado(@PathVariable EstadoBoleta estado) {
        return service.listarPorEstado(estado);
    }

    @GetMapping("/mias")
    public List<BoletaResponseDTO> listarMias(Authentication auth) {
        return service.listarPorEmail(auth.getName());
    }
    

    @PostMapping
    public ResponseEntity<BoletaResponseDTO> crear(@Valid @RequestBody BoletaRequestDTO dto, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto, auth.getName()));
    }

    @PatchMapping("/{id}/estado")
    public BoletaResponseDTO cambiarEstado(@PathVariable Long id, @RequestParam EstadoBoleta estado) {
        return service.cambiarEstado(id, estado);
    }
}