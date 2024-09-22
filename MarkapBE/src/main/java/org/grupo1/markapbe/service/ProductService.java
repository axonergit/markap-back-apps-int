package org.grupo1.markapbe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.grupo1.markapbe.controller.dto.ProductDTO;
import org.grupo1.markapbe.controller.dto.ProductResponseDTO;
import org.grupo1.markapbe.persistence.entity.CategoryEntity;
import org.grupo1.markapbe.persistence.entity.ProductEntity;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.CategoryRepository;
import org.grupo1.markapbe.persistence.repository.ProductRepository;
import org.grupo1.markapbe.persistence.repository.VisitedProductsRepository;
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
    private UserService usuarioService;

    @Autowired
    private VisitedProductService visitedProductService;

    @Autowired
    private ObjectMapper objectMapper;



    public List<ProductResponseDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(this::convertToDtoResponse)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponseDTO> getProductoById(Long id) {
        Optional<ProductEntity> producto = productoRepository.findById(id);
        try {
            UserEntity usuario = usuarioService.obtenerUsuarioPeticion();

            if (producto.isPresent()) {
                visitedProductService.createVisitedProduct(producto.get());
                return Optional.of(convertToDtoResponse(producto.get()));
            } else {
                return Optional.empty();
            }
        } catch (EntityNotFoundException e) {
            return Optional.empty();
        }
    }



    public List<ProductResponseDTO> getProductosByIdCategoria(Long id) {
        return productoRepository.findByCategoria_id(id) // Busca los productos por id de categoría
                .stream() // Convierte la lista a un Stream
                .map(this::convertToDtoResponse) // Convierte cada ProductEntity a ProductDTO de respuesta
                .collect(Collectors.toList()); // Junta el resultado en una lista
    }

    public List<ProductResponseDTO> getFeaturedproducts() { // obtener productos destacados
        return productoRepository.findByDestacadoTrue() // Busca los productos por campo "destacado" = true
                .stream()
                .map(this::convertToDtoResponse)
                .collect(Collectors.toList());
    }


    public ProductResponseDTO createProducto(ProductDTO productoRequestDTO) {
        CategoryEntity categoria = categoriaRepository.findById(productoRequestDTO.categoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoria not found"));
        UserEntity userCreador = usuarioService.obtenerUsuarioPeticion();
        ProductEntity productoCreado = convertToEntity(productoRequestDTO,userCreador, categoria);
        return convertToDtoResponse(productoRepository.save(productoCreado)); // lo guardamos en la DB
    }


    //revisar

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
        return objectMapper.convertValue(producto, ProductDTO.class);
    }


    private ProductResponseDTO convertToDtoResponse(ProductEntity producto) {
        return new ProductResponseDTO(
                producto.getId(),
                producto.getImagen(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getDetalles(),
                producto.getStock(),
                producto.getCategoria().getNombreCategoria(),
                producto.getUser().getUsername());
    }

    public ProductEntity convertToEntity(ProductDTO productoRequestDTO, UserEntity user, CategoryEntity categoria) {
        return ProductEntity.builder()
                .imagen(productoRequestDTO.imagen())
                .descripcion(productoRequestDTO.descripcion())
                .precio(productoRequestDTO.precio())
                .detalles(productoRequestDTO.detalles())
                .stock(productoRequestDTO.stock())
                .user(user)
                .categoria(categoria)
                .destacado(true)    // definir como hacemos que un producto sea destacado o no, dejo asi para probar
                .build();
    }

    public ProductEntity getEntityById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));
    }
}