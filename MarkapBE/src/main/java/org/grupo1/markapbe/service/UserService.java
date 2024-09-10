package org.grupo1.markapbe.service;


import jakarta.persistence.EntityNotFoundException;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;



    public UserEntity obtenerUsuarioPeticion(){
        String userData = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserEntityByUsername(userData)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El usuario no fue encontrado. Tal vez llamaste a esta peticion desde un endpoint" +
                        " no protegido por autorizacion."));
    }
}
