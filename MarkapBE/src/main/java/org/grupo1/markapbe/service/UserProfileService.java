package org.grupo1.markapbe.service;


import org.grupo1.markapbe.controller.dto.UserProfileDTO.UserDetailsResponse;
import org.grupo1.markapbe.controller.dto.UserProfileDTO.UserProfileUpdateDTO;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.entity.UserProfileEntity;
import org.grupo1.markapbe.persistence.repository.UserProfileRepository;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserService userService;

    public UserDetailsResponse getUserDetails(String username) {

        UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No se encontro el usuario en la base de datos."));

        UserProfileEntity userProfileEntity = userProfileRepository.findUserProfileEntityByUser(userEntity).orElseThrow(() -> new UsernameNotFoundException("No fue posible obtener los detalles del usuario. (Si usaste las cuentas por defecto estas no tienen cargados los detalles)"));
        return new UserDetailsResponse(userProfileEntity.getName(), userProfileEntity.getEmail(), userProfileEntity.getBirthDate());
    }

    public UserDetailsResponse updateUserDetails(UserProfileUpdateDTO nuevoscambios) {

        UserEntity userEntity = userService.obtenerUsuarioPeticion();

        UserProfileEntity userProfileEntity = userProfileRepository.findUserProfileEntityByUser(userEntity).orElseThrow(() -> new UsernameNotFoundException("No fue posible obtener los detalles del usuario. (Si usaste las cuentas por defecto estas no tienen cargados los detalles)"));

        userProfileEntity.setName(nuevoscambios.name());
        userProfileEntity.setLastName(nuevoscambios.lastName());
        userProfileEntity.setBirthDate(nuevoscambios.birthDate());
        UserProfileEntity userprofile = userProfileRepository.save(userProfileEntity);
        return new UserDetailsResponse(userprofile.getName() + " " +  userprofile.getLastName(), userprofile.getEmail(), userprofile.getBirthDate());
    }




}
