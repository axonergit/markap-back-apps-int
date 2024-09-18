package org.grupo1.markapbe.controller;

import org.grupo1.markapbe.controller.dto.VisitedProductDTO;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.grupo1.markapbe.service.UserService;
import org.grupo1.markapbe.service.VisitedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/productos/visited")
public class VisitedProductsController {

    @Autowired
    private UserService userService;

    @Autowired
    private VisitedProductService visitedProductService;

    @GetMapping("/{page}")
    public ResponseEntity<List<VisitedProductDTO>> getVisitedProducts(@PathVariable int page) {
        return new ResponseEntity<>(visitedProductService.getVisited(page), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<VisitedProductDTO>> getAllVisitedProducts() {
        return new ResponseEntity<>(visitedProductService.getVisitedAll(), HttpStatus.OK);
    }
}
