package org.grupo1.markapbe.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/method")
@PreAuthorize("denyAll()")
public class TestAuthController {



    @Operation( description = "ESTE METODO ES PARA PROBAR EL GET.")
    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloGet(){
        return "Hello World - GET";
    }

    @PostMapping("/post")
    public String helloPost(){
        return "Hello World - POST";
    }

    @PutMapping("/put")
    public String helloPut(){
        return "Hello World - PUT";
    }

    @DeleteMapping("/delete")
    public String helloDelete(){
        return "Hello World - DELETE";
    }

    @PatchMapping("/patch")
    public String helloPatch(){
        return "Hello World - PATCH";
    }

}
