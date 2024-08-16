package org.grupo1.markapbe.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/method")
public class TestAuthController {



    @Operation( description = "ESTE METODO ES PARA PROBAR EL GET.")
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('READ')")
    public String helloGet(Principal principal){
        return "Hello World - GET " + principal.getName();
    }


    @PostMapping("/post")
    @PreAuthorize("hasRole('USUARIO')")
    public String helloPost(Principal principal){
        return "Hello World - POST " + principal.getName();
    }

    @PutMapping("/put")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloPut(Principal principal){
        return "Hello World - PUT " + principal.getName();
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
