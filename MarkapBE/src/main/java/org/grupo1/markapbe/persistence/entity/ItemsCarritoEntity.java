package org.grupo1.markapbe.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "itemsCarrito")
public class ItemsCarritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private CarritoEntity carrito;

    //Producto Column faltante FK

    @Column(name = "amount")
    private int amount;
}
