package org.grupo1.markapbe.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.grupo1.markapbe.controller.dto.FavoriteProductRequestDTO;
import org.grupo1.markapbe.controller.dto.ProductDTO;
import org.grupo1.markapbe.persistence.entity.CategoryEntity;
import org.grupo1.markapbe.persistence.entity.FavoriteProductsEntity;
import org.grupo1.markapbe.persistence.entity.ProductEntity;
import org.grupo1.markapbe.persistence.repository.CategoryRepository;
import org.grupo1.markapbe.persistence.repository.FavoriteProductsRepository;
import org.grupo1.markapbe.persistence.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class FavoriteProductService {

    @Autowired
    private FavoriteProductsRepository productosFavoritosRepository;

    @Autowired
    private ProductRepository productosRepository;

    @Autowired
    private ObjectMapper objectMapper;


    //create
    public FavoriteProductRequestDTO createFavoriteProduct(FavoriteProductRequestDTO productoRequestDTO) {
        FavoriteProductsEntity fprod = convertToEntity(productoRequestDTO);
        return convertToDto(productosFavoritosRepository.save(fprod));
    }


    public List<ProductDTO> getFavoriteProductsByUserId(Long userId) {

        ArrayList<Optional<ProductEntity>> listaDetalleProductos = new ArrayList<>();

        List<Long> listaProductosID = productosFavoritosRepository.findFavoriteProductsByUserId(userId);

        for (Long id : listaProductosID) {
            Optional<ProductEntity> productoCompleto = productosRepository.findById(id);
            listaDetalleProductos.add(productoCompleto);
        }

        return listaDetalleProductos.stream() // Convierte la lista a un Stream
                .map(this::convertToDtoProductoCompleto) // Convierte cada ProductEntity a ProductDTO
                .collect(Collectors.toList()); // Recoge el resultado en una lista
    }


    // metodos para manejar DTOs
    private ProductDTO convertToDtoProductoCompleto(Optional<ProductEntity> producto) {
        return objectMapper.convertValue(producto,ProductDTO.class);

    }

    private FavoriteProductRequestDTO convertToDto(FavoriteProductsEntity fprod) {
        return objectMapper.convertValue(fprod, FavoriteProductRequestDTO.class);
    }

    private FavoriteProductsEntity convertToEntity(FavoriteProductRequestDTO dto) {
        return objectMapper.convertValue(dto,FavoriteProductsEntity.class);
    }
}
