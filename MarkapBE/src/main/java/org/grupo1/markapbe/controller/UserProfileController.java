package org.grupo1.markapbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.grupo1.markapbe.controller.dto.UserProfileDTO.*;
import org.grupo1.markapbe.controller.dto.UserProfileDTO.UserDetailsResponse;
import org.grupo1.markapbe.controller.dto.UserProfileDTO.UserProfileUpdateDTO;
import org.grupo1.markapbe.controller.dto.VisitedProductDTO;
import org.grupo1.markapbe.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Operation(summary = "Obtener perfil de usuario",
            description = "Este endpoint permite obtener los detalles del perfil de un usuario a partir de su nombre de usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles del usuario obtenidos exitosamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el nombre de usuario especificado.")
    })
    @GetMapping("/{username}")
    public ResponseEntity<UserDetailsResponse> getUserProfile(@PathVariable String username) {
        return new ResponseEntity<>(userProfileService.getUserDetails(username), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar perfil de usuario",
            description = "Este endpoint permite actualizar los detalles del perfil de un usuario autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil del usuario actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o no se pudo obtener los detalles del usuario.")
    })
    @PutMapping("/")
    public ResponseEntity<UserDetailsResponse> updateUserProfile(@RequestBody UserProfileUpdateDTO userProfileUpdateDTO) {
        return new ResponseEntity<>(userProfileService.updateUserDetails(userProfileUpdateDTO), HttpStatus.OK);
    }



}
