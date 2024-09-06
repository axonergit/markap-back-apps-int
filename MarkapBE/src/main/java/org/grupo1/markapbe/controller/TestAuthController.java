package org.grupo1.markapbe.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.grupo1.markapbe.controller.dto.ErrorResponseDTO;
import org.grupo1.markapbe.service.UserDetailServiceImpl;
import org.grupo1.markapbe.service.UserProfileService;
import org.grupo1.markapbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/method")
public class TestAuthController {


    @Autowired
    private UserProfileService userProfileService;


    @Autowired
    private UserService userService;

    @Operation(description = "ESTE METODO ES PARA PROBAR EL GET.")
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('READ')")
    public String helloGet() {
        return "Hello World - GET " + userService.obtenerUsuarioPeticion().getUsername();
    }


    @PostMapping("/post")
    @PreAuthorize("hasRole('USUARIO')")
    public String helloPost() {
        return "Hello World - POST " + userService.obtenerUsuarioPeticion().getUsername();
    }

    @PutMapping("/put")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloPut(Principal principal) {
        return "Hello World - PUT " + principal.getName();
    }

    @DeleteMapping("/delete")
    public String helloDelete() {
        return "Hello World - DELETE";
    }

    @PatchMapping("/patch")
    public String helloPatch() {
        return "Hello World - PATCH";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getDetails")
    public ResponseEntity<?> getDetails(Principal principal) {

        try {
            return new ResponseEntity<>(userProfileService.getUserDetails(principal.getName()), HttpStatus.OK);
        } catch (Exception ex) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage());

            return new ResponseEntity<>(errorResponseDTO, HttpStatus.CONFLICT);
        }
    }
}
