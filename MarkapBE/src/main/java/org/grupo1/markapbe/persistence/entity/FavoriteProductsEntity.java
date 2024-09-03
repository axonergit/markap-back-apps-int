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
@Table(name = "FavoriteProducts", uniqueConstraints = @UniqueConstraint(columnNames = {"id_user", "id_producto"}))
public class FavoriteProductsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_user", nullable = false)
    private Long id_user;

    @Column(name = "id_producto", nullable = false)
    private Long id_product;


    @ManyToOne
    @JoinColumn(name = "id_user",insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "id_producto", insertable = false, updatable = false)
    private ProductEntity product;
}
