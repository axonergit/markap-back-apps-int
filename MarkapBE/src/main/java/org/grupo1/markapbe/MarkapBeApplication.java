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
    CommandLineRunner init(UserRepository userRepository) {
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


            RoleEntity roleUser = RoleEntity.builder().roleEnum(RoleEnum.USUARIO).permissionSet(Set.of(readPermission, suscribePermission)).build();
            RoleEntity roleAdmin = RoleEntity.builder().roleEnum(RoleEnum.ADMIN).permissionSet(Set.of(readPermission, suscribePermission, createPermission, deletePermission)).build();

            UserEntity userMaestro = UserEntity.builder().username("master").password("$2a$10$cTxbkyr.IN.hn/m5iraiCeIo4XogBvctfbEFcWDdgFx/35HX5ynAC").isEnabled(true).accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true).roles(Set.of(roleAdmin)).build();
            UserEntity userComun = UserEntity.builder().username("usuario").password("$2a$10$mi4iWTtebYuKT.jApPXKcuBeYI3yiftz3hIFskaJ8VXMbsT0Wn7R2").isEnabled(true).accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true).roles(Set.of(roleUser)).build();

            userRepository.saveAll(List.of(userMaestro, userComun));

            //guardamos las cateorias en las tablas

        };
    }

}
