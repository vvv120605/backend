package dev.teammusic.SonidoVivo_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductoRequestDTO {

    @NotBlank
    @Size (max = 50)
    private String codigo;

    @NotBlank
    @Size (max = 100)
    private String nombre;

    private String descripcion;

    @NotNull
    @PositiveOrZero 
    private Integer stock;

    @NotNull
    @PositiveOrZero
    private Integer stockCritico;

    @Size(max = 255)
    private String imagen;

    @NotNull
    @Positive
    private Long categoriaId;

    @NotNull
    @PositiveOrZero
    private Integer precio;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockCritico() {
        return stockCritico;
    }

    public void setStockCritico(Integer stockCritico) {
        this.stockCritico = stockCritico;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

}
