package org.grupo1.markapbe.service;

import org.grupo1.markapbe.controller.dto.ProductRequestDTO;
import org.grupo1.markapbe.controller.dto.ProductResponseDTO;
import org.grupo1.markapbe.persistence.entity.CategoryEntity;
import org.grupo1.markapbe.persistence.entity.ProductEntity;
import org.grupo1.markapbe.persistence.repository.CategoryRepository;
import org.grupo1.markapbe.persistence.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productoRepository;

    @Autowired
    private CategoryRepository categoriaRepository;

    public List<ProductResponseDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponseDTO> getProductoById(Long id) {
        return productoRepository.findById(id)
                .map(this::convertToDto);
    }

    public ProductResponseDTO createProducto(ProductRequestDTO productoRequestDTO) {
        //CategoryEntity categoria = categoriaRepository.findById(productoRequestDTO.getIdCategoria())
               // .orElseThrow(() -> new RuntimeException("Categoria not found"));
        ProductEntity producto = convertToEntity(productoRequestDTO);  // agregar categoria como parameto (,categoria)
        return convertToDto(productoRepository.save(producto));
    }

    public Optional<ProductResponseDTO> updateProducto(Long id, ProductRequestDTO productoRequestDTO) {
        return productoRepository.findById(id).map(producto -> {
            //CategoryEntity categoria = categoriaRepository.findById(productoRequestDTO.getIdCategoria())
                    //.orElseThrow(() -> new RuntimeException("Categoria not found"));
            producto.setImagen(productoRequestDTO.getImagen());
            producto.setDescripcion(productoRequestDTO.getDescripcion());
            producto.setPrecio(productoRequestDTO.getPrecio());
            producto.setDetalles(productoRequestDTO.getDetalles());
            producto.setStock(productoRequestDTO.getStock());
            //producto.setCategoria(categoria);
            return convertToDto(productoRepository.save(producto));
        });
    }

    public boolean deleteProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ProductResponseDTO convertToDto(ProductEntity producto) {
        return new ProductResponseDTO(
                producto.getId(),
                producto.getImagen(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getDetalles(),
                producto.getStock()
                //producto.getCategoria().getNombreCategoria()
        );
    }

    private ProductEntity convertToEntity(ProductRequestDTO dto) {    //Agregar como parámetro ( ,CategoryEntity categoria)
        ProductEntity producto = new ProductEntity();
        producto.setImagen(dto.getImagen());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setDetalles(dto.getDetalles());
        producto.setStock(dto.getStock());
        //producto.setCategoria(categoria);

        return producto;
    }
}

