package org.grupo1.markapbe.service;


import org.grupo1.markapbe.controller.dto.UserDetailsResponse;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.entity.UserProfileEntity;
import org.grupo1.markapbe.persistence.repository.UserProfileRepository;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserDetailsResponse getUserDetails(String username) {

        UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No se encontro el usuario en la base de datos."));

        UserProfileEntity userProfileEntity = userProfileRepository.findUserProfileEntityByUser(userEntity).orElseThrow(() -> new UsernameNotFoundException("No fue posible obtener los detalles del usuario. (Si usaste las cuentas por defecto estas no tienen cargados los detalles)"));
        return new UserDetailsResponse(userProfileEntity.getFullName(), userProfileEntity.getEmail());
    }
}
