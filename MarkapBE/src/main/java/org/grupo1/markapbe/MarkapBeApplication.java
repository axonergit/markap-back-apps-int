package org.grupo1.markapbe;

import org.grupo1.markapbe.persistence.entity.*;
import org.grupo1.markapbe.persistence.repository.CategoryRepository;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class MarkapBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarkapBeApplication.class, args);
    }


    @Bean
    CommandLineRunner init(UserRepository userRepository, CategoryRepository categoryRepository) {
        return args -> {
            PermissionEntity createPermission = PermissionEntity.builder()
                    .name("CREATE")
                    .build();
            PermissionEntity readPermission = PermissionEntity.builder()
                    .name("READ")
                    .build();
            PermissionEntity deletePermission = PermissionEntity.builder()
                    .name("DELETE")
                    .build();
            PermissionEntity suscribePermission = PermissionEntity.builder()
                    .name("SUSCRIBE")
                    .build();

            //Todo lo que tiene que ver con la creación de categorías
            CategoryEntity category1 = CategoryEntity.builder()
                    .nombreCategoria("Figuras de Acción")
                    .build();
            CategoryEntity category2 = CategoryEntity.builder()
                    .nombreCategoria("Cómics y Mangas")
                    .build();
            CategoryEntity category3 = CategoryEntity.builder()
                    .nombreCategoria("Monedas y Billetes")
                    .build();
            CategoryEntity category4 = CategoryEntity.builder()
                    .nombreCategoria("Autógrafos")
                    .build();
            CategoryEntity category5 = CategoryEntity.builder()
                    .nombreCategoria("Juguetes Vintage")
                    .build();
            CategoryEntity category6 = CategoryEntity.builder()
                    .nombreCategoria("Cartas Coleccionables")
                    .build();
            CategoryEntity category7 = CategoryEntity.builder()
                    .nombreCategoria("Arte")
                    .build();
            CategoryEntity category8 = CategoryEntity.builder()
                    .nombreCategoria("Antiguedades")
                    .build();
            CategoryEntity category9 = CategoryEntity.builder()
                    .nombreCategoria("Música y Vinilos")
                    .build();
            CategoryEntity category10 = CategoryEntity.builder()
                    .nombreCategoria("Libros Raros")
                    .build();
            CategoryEntity category11 = CategoryEntity.builder()
                    .nombreCategoria("Relojes y Joyería")
                    .build();
            CategoryEntity category12 = CategoryEntity.builder()
                    .nombreCategoria("Fotografía")
                    .build();

            RoleEntity roleUser = RoleEntity.builder().roleEnum(RoleEnum.USUARIO).permissionSet(Set.of(readPermission, suscribePermission)).build();
            RoleEntity roleAdmin = RoleEntity.builder().roleEnum(RoleEnum.ADMIN).permissionSet(Set.of(readPermission, suscribePermission, createPermission, deletePermission)).build();

            UserEntity userMaestro = UserEntity.builder().username("master").password("$2a$10$cTxbkyr.IN.hn/m5iraiCeIo4XogBvctfbEFcWDdgFx/35HX5ynAC").isEnabled(true).accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true).roles(Set.of(roleAdmin)).build();
            UserEntity userComun = UserEntity.builder().username("usuario").password("$2a$10$mi4iWTtebYuKT.jApPXKcuBeYI3yiftz3hIFskaJ8VXMbsT0Wn7R2").isEnabled(true).accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true).roles(Set.of(roleUser)).build();

            userRepository.saveAll(List.of(userMaestro, userComun));

            //guardamos las cateorias en las tablas
            categoryRepository.saveAll(List.of(category1, category2));
        };
    }

}
