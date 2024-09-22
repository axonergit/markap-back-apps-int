package org.grupo1.markapbe.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.grupo1.markapbe.controller.dto.FavoriteProductRequestDTO;
import org.grupo1.markapbe.controller.dto.CatalogoDTO.ProductDTO;
import org.grupo1.markapbe.persistence.entity.FavoriteProductsEntity;
import org.grupo1.markapbe.persistence.entity.ProductEntity;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.FavoriteProductsRepository;
import org.grupo1.markapbe.persistence.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class FavoriteProductService {

    @Autowired
    private FavoriteProductsRepository productosFavoritosRepository;

    @Autowired
    private ProductRepository productosRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;


    public List<ProductDTO> getLikes(){
        UserEntity userEntity = userService.obtenerUsuarioPeticion();
        List<FavoriteProductsEntity> productos = productosFavoritosRepository.findFavoriteProductsEntitiesByUser(userEntity).orElseThrow(() -> new EntityNotFoundException("No existen likes"));
        return productos.stream()
                .map(FavoriteProductsEntity::getProduct)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public FavoriteProductRequestDTO createFavoriteProduct(long productID) {
        UserEntity userEntity = userService.obtenerUsuarioPeticion();
        ProductEntity product = productosRepository.findById(productID).orElseThrow(() -> new EntityNotFoundException("El producto no existe"));
        FavoriteProductsEntity nuevoLike = FavoriteProductsEntity.builder().product(product).user(userEntity).build();
        FavoriteProductsEntity likeGuardado = productosFavoritosRepository.save(nuevoLike);
        return new FavoriteProductRequestDTO(likeGuardado.getId());
    }

    private ProductDTO convertToDto(ProductEntity producto) {
        return objectMapper.convertValue(producto,ProductDTO.class);
    }

    public void eliminarProductoFavorito(Long productId) {
        UserEntity userEntity = userService.obtenerUsuarioPeticion();
        FavoriteProductsEntity favorito = productosFavoritosRepository
                .findByUserAndProductId(userEntity, productId)
                .orElseThrow(() -> new EntityNotFoundException("El producto no está en la lista de favoritos"));
        productosFavoritosRepository.delete(favorito);
    }

}