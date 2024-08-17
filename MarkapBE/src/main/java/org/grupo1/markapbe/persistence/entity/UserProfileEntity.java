package org.grupo1.markapbe.persistence.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "user_profile")
public class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(name = "email",updatable = false,unique = true)
    private String email;

    @Column(name = "full_name")
    private String fullName;


    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
