package org.grupo1.markapbe.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "formularios")
public class FormularioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;

    private String problematica;

    @ElementCollection
    @CollectionTable(name = "fotos_formulario", joinColumns = @JoinColumn(name = "formulario_id"))
    @Column(name = "foto")
    private List<String> fotoBase64;

    private String descripcion;

}
