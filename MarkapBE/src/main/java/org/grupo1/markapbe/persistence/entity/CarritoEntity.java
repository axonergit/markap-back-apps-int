package org.grupo1.markapbe.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "carrito")
public class CarritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_status")
    private boolean paymentStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity User;

    @OneToMany
    @JoinColumn(name = "items_carrito")
    private Set<ItemsCarritoEntity> itemsCarrito;
}
