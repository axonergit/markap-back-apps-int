package org.grupo1.markapbe.service;

import org.grupo1.markapbe.controller.dto.VisitedProductDTO;
import org.grupo1.markapbe.persistence.entity.ProductEntity;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.entity.VisitedProductsEntity;
import org.grupo1.markapbe.persistence.repository.VisitedProductsRepository;
import org.grupo1.markapbe.persistence.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitedProductService {

    @Autowired
    private VisitedProductsRepository visitedProductsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    public List<VisitedProductDTO> getVisited(int page) {
        UserEntity user = userService.obtenerUsuarioPeticion();
        Pageable objetoPageable = PageRequest.of(page,10, Sort.by("fecha").descending());
        List<VisitedProductsEntity> visitedProducts = visitedProductsRepository.findVisitedProductsEntityByUser(user,objetoPageable);
        return visitedProducts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<VisitedProductDTO> getVisitedAll() {
        UserEntity user = userService.obtenerUsuarioPeticion();
        List<VisitedProductsEntity> visitedProducts = visitedProductsRepository.findVisitedProductsEntityByUser(user);
        return visitedProducts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public boolean createVisitedProduct(ProductEntity producto) {
        UserEntity user = userService.obtenerUsuarioPeticion();
        VisitedProductsEntity nuevoVisitado;
        Optional<VisitedProductsEntity> productoVisitado = visitedProductsRepository.findVisitedProductsEntityByUserAndProduct(user,producto);
        if(productoVisitado.isPresent()) {
            VisitedProductsEntity visitedProductsEntity = productoVisitado.get();
            visitedProductsEntity.setFecha(new Timestamp(System.currentTimeMillis()));
            nuevoVisitado = visitedProductsEntity;
        } else {
            nuevoVisitado = VisitedProductsEntity.builder()
                    .fecha(new Timestamp(System.currentTimeMillis()))
                    .product(producto)
                    .user(user)
                    .build();
        }

        VisitedProductsEntity visitadoGuardado = visitedProductsRepository.save(nuevoVisitado);

        return true;
    }

    private VisitedProductDTO convertToDto(VisitedProductsEntity visitedProduct) {
        return new VisitedProductDTO(visitedProduct.getProduct());
    }
}
