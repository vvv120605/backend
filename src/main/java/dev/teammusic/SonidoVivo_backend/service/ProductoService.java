package dev.teammusic.SonidoVivo_backend.service;
 
import java.util.List;
 
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
 
import dev.teammusic.SonidoVivo_backend.dto.ProductoRequestDTO;
import dev.teammusic.SonidoVivo_backend.dto.ProductoResponseDTO;
import dev.teammusic.SonidoVivo_backend.model.Producto;
import dev.teammusic.SonidoVivo_backend.repository.CategoriaRepository;
import dev.teammusic.SonidoVivo_backend.repository.ProductoRepository;
import jakarta.transaction.Transactional;
 
@Service
@Transactional
public class ProductoService {
 
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
 
    public ProductoService(ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }
 
    // Listar todos los productos
    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }
 
    // Obtener por ID
    public ProductoResponseDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        return toResponse(producto);
    }
 
    // Listar por categoría (agregado desde la versión de la rama isaac)
    public List<ProductoResponseDTO> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId).stream()
                .map(this::toResponse)
                .toList();
    }
 
    // Crear producto
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        Producto producto = new Producto();
        copiarDatos(dto, producto);
        return toResponse(productoRepository.save(producto));
    }
 
    // Actualizar producto
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        copiarDatos(dto, producto);
        return toResponse(productoRepository.save(producto));
    }
 
    // Eliminar producto
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }
 
    // Métodos auxiliares
 
    private void copiarDatos(ProductoRequestDTO dto, Producto producto) {
        productoRepository.findByCodigo(dto.getCodigo())
                .filter(productoExistente -> !productoExistente.getId().equals(producto.getId()))
                .ifPresent(productoExistente -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con ese código");
                });
 
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setStock(dto.getStock());
        producto.setStockCritico(dto.getStockCritico());
        producto.setImagen(dto.getImagen());
        producto.setCategoria(categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada")));
        producto.setPrecio(dto.getPrecio());
    }
 
    private ProductoResponseDTO toResponse(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setCodigo(producto.getCodigo());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setStock(producto.getStock());
        dto.setStockCritico(producto.getStockCritico());
        dto.setImagen(producto.getImagen());
        dto.setCategoriaId(producto.getCategoria().getId());
        dto.setCategoriaNombre(producto.getCategoria().getNombre());
        dto.setPrecio(producto.getPrecio());
        return dto;
    }
}