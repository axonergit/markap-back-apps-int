package org.grupo1.markapbe.controller.dto;

public class ProductRequestDTO {

    private String imagen;
    private String descripcion;
    private Float precio;
    private String detalles;
    private int stock;
    //private Long idCategoria;

    // Constructor vacío
    public ProductRequestDTO() {}

    // Constructor con todos los campos

    //AGREGAR LA CATEGORIA EN EL CONSTRUCTOR
    public ProductRequestDTO(String imagen, String descripcion, Float precio,
                              String infoAdicional, int stock) {
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.precio = precio;
        this.detalles = detalles;
        this.stock = stock;
        //this.idCategoria = idCategoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    /*
    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

     */
}
