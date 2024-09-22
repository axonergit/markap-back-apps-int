package org.grupo1.markapbe.persistence.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


//IMPORTANTE: VER COMO HACER PARA QUE NO SE REPITAN FILAS CON MISMO ID_USER Y MISMO ID_PRODUCT


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "FavoriteProducts", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
public class FavoriteProductsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

}