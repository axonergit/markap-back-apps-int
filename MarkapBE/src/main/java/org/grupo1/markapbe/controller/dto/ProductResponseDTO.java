package org.grupo1.markapbe.controller.dto;

public class ProductResponseDTO {

    private Long idProducto;
    private String imagen;
    private String descripcion;
    private Float precio;
    private String infoAdicional;
    private int stock;
    private String categoriaNombre;

    // Constructor con todos los campos

    //AGREGAR LA CATEGORIA EN EL CONSTRUCTOR
    public ProductResponseDTO(Long idProducto, String imagen, String descripcion, Float precio,
                               String infoAdicional, int stock) {
        this.idProducto = idProducto;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.precio = precio;
        this.infoAdicional = infoAdicional;
        this.stock = stock;
        //this.categoriaNombre = categoriaNombre;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(String infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }
}
