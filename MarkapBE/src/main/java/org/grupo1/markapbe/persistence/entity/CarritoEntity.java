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

    @Column(name = "payment_status", nullable = false)
    private boolean paymentStatus = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private UserEntity User;

    @OneToMany
    @JoinColumn(name = "items_carrito")
    private Set<ItemsCarritoEntity> itemsCarrito;
}
