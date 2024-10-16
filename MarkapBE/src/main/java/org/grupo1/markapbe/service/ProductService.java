package org.grupo1.markapbe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.grupo1.markapbe.controller.dto.CatalogoDTO.ProductDTO;
import org.grupo1.markapbe.controller.dto.CatalogoDTO.ProductRequestUpdateDTO;
import org.grupo1.markapbe.controller.dto.CatalogoDTO.ProductResponseDTO;
import org.grupo1.markapbe.persistence.entity.CategoryEntity;
import org.grupo1.markapbe.persistence.entity.ProductEntity;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.CategoryRepository;
import org.grupo1.markapbe.persistence.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


    public Page<ProductResponseDTO> getProductosByIdCategoria(Long id, Pageable pageable) {
        return productoRepository.findByCategoria_id(id, pageable) // Busca los productos por id de categoría con paginación
                .map(this::convertToDtoResponse); // Convierte cada ProductEntity a ProductDTO de respuesta
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

    public ProductResponseDTO updateProducto(Long id, ProductRequestUpdateDTO productoRequestUpdateDTO) {
        ProductEntity producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        CategoryEntity categoria = categoriaRepository.findById(productoRequestUpdateDTO.categoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoria not found"));
        UserEntity userCreador = usuarioService.obtenerUsuarioPeticion();

        if (producto.getUser() == userCreador) {
            producto.setImagen(productoRequestUpdateDTO.imagen());
            producto.setDescripcion(productoRequestUpdateDTO.descripcion());
            producto.setPrecio(productoRequestUpdateDTO.precio());
            producto.setStock(productoRequestUpdateDTO.stock());
            producto.setCategoria(categoria);

            productoRepository.save(producto);

            return convertToDtoResponse(producto);
        }

        else {
            throw new RuntimeException("Usuario no es el propietario del producto");
        }
    }

    public boolean consumeStock(Long id, int quantity) {
        ProductEntity producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setStock(producto.getStock() - quantity);

        productoRepository.save(producto);

        return true;
    }


    public boolean deleteProducto(Long id) {
        ProductEntity producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        UserEntity userCreador = usuarioService.obtenerUsuarioPeticion();
        if (producto.getUser() == userCreador) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean featureProduct(Long id) {
        ProductEntity producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setDestacado(true);
        productoRepository.save(producto);

        return true;
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