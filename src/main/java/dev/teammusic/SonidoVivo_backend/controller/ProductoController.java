package dev.teammusic.SonidoVivo_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.teammusic.SonidoVivo_backend.dto.ProductoRequestDTO;
import dev.teammusic.SonidoVivo_backend.dto.ProductoResponseDTO;
import dev.teammusic.SonidoVivo_backend.service.ProductoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoResponseDTO> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ProductoResponseDTO obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<ProductoResponseDTO> listarPorCategoria(@PathVariable Long categoriaId) {
        return service.listarPorCategoria(categoriaId);
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ProductoResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequestDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}