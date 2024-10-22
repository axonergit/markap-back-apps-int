package org.grupo1.markapbe.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "imagen")
    private String imagen;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private BigDecimal precio;

    @Column(name = "detalles")
    private String detalles;

    @Column(name = "stock")
    private int stock;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_creador_id")  // El nombre de la columna que contendrá la clave foránea en la tabla actual
    private UserEntity user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    @JsonBackReference  // maneja mucho mejor las relaciones bidireccionales, para tener en cuenta
    //@JsonManagedReference y @JsonBackReference trabajan juntas para gestionar la serialización de relaciones bidireccionales, evitando ciclos infinitos.
    private CategoryEntity categoria;

    private boolean destacado;

}
