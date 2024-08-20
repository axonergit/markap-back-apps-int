package org.grupo1.markapbe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.grupo1.markapbe.controller.dto.ProductDTO;
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

    @Autowired
    private ObjectMapper objectMapper;

    public List<ProductDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ProductDTO> getProductoById(Long id) {
        return productoRepository.findById(id)
                .map(this::convertToDto);
    }

    public ProductDTO createProducto(ProductDTO productoRequestDTO) {
        CategoryEntity categoria = categoriaRepository.findById(productoRequestDTO.categoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoria not found"));
        ProductEntity producto = convertToEntity(productoRequestDTO);  // agregar categoria como parameto (,categoria)
        return convertToDto(productoRepository.save(producto));
    }

    public Optional<ProductDTO> updateProducto(Long id, ProductDTO productoRequestDTO) {
        return productoRepository.findById(id).map(producto -> {
            CategoryEntity categoria = categoriaRepository.findById(productoRequestDTO.categoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoria not found"));
            producto.setImagen(productoRequestDTO.imagen());
            producto.setDescripcion(productoRequestDTO.descripcion());
            producto.setPrecio(productoRequestDTO.precio());
            producto.setDetalles(productoRequestDTO.detalles());
            producto.setStock(productoRequestDTO.stock());
            producto.setCategoria(categoria);
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

    private ProductDTO convertToDto(ProductEntity producto) {
        return objectMapper.convertValue(producto,ProductDTO.class);
    }

    private ProductEntity convertToEntity(ProductDTO dto) {
        return objectMapper.convertValue(dto,ProductEntity.class);
    }
}

