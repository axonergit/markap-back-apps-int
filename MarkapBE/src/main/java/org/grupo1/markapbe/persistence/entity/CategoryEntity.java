package org.grupo1.markapbe.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_categoria",unique = true)
    private String nombreCategoria;


    @OneToMany(mappedBy = "categoria")
    @JsonManagedReference // maneja mucho mejor las relaciones bidireccionales, para tener en cuenta
    //@JsonManagedReference y @JsonBackReference trabajan juntas para gestionar la serialización de relaciones bidireccionales, evitando ciclos infinitos.
    private Set<ProductEntity> productos;

}
