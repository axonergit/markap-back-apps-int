package org.grupo1.markapbe.persistence.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestAuthController {



    @GetMapping("/inseguro")
    public ResponseEntity<String> sinAuth() {
        return new ResponseEntity<String>("Bienvenido sin auth", HttpStatus.ACCEPTED);
    }


    @GetMapping("/seguro")
    public ResponseEntity<String> conAuth() {
        return new ResponseEntity<String>("Bienvenido con auth", HttpStatus.ACCEPTED);
    }

}
