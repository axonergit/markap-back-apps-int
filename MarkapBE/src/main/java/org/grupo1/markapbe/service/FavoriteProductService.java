package org.grupo1.markapbe.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.grupo1.markapbe.controller.dto.FavoriteProductRequestDTO;
import org.grupo1.markapbe.controller.dto.ProductDTO;
import org.grupo1.markapbe.persistence.entity.CategoryEntity;
import org.grupo1.markapbe.persistence.entity.FavoriteProductsEntity;
import org.grupo1.markapbe.persistence.entity.ProductEntity;
import org.grupo1.markapbe.persistence.entity.UserEntity;
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



    public List<ProductDTO> getLikes(UserEntity userEntity){
        List<FavoriteProductsEntity> productos = productosFavoritosRepository.findFavoriteProductsEntitiesByUser(userEntity).orElseThrow(() -> new EntityNotFoundException("No existen likes"));
        return productos.stream()
                .map(FavoriteProductsEntity::getProduct)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    //create
    public FavoriteProductRequestDTO createFavoriteProduct(FavoriteProductRequestDTO productoRequestDTO, UserEntity usuarioLikeador) {
        ProductEntity product = productosRepository.findById(productoRequestDTO.id_product()).orElseThrow(() -> new EntityNotFoundException("El producto no existe"));
        FavoriteProductsEntity nuevoLike = FavoriteProductsEntity.builder().product(product).user(usuarioLikeador).build();
        FavoriteProductsEntity likeGuardado = productosFavoritosRepository.save(nuevoLike);
        return new FavoriteProductRequestDTO(likeGuardado.getId());
    }

    private ProductDTO convertToDto(ProductEntity producto) {
        return objectMapper.convertValue(producto,ProductDTO.class);
    }



}