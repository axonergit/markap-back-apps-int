package org.grupo1.markapbe.controller;


import jakarta.validation.Valid;
import org.grupo1.markapbe.controller.dto.AuthCreateUserRequest;
import org.grupo1.markapbe.controller.dto.AuthLoginRequest;
import org.grupo1.markapbe.controller.dto.AuthResponse;
import org.grupo1.markapbe.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


    @Autowired
    private UserDetailServiceImpl userDetailService;



    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthCreateUserRequest authCreateUserRequest){
        return new ResponseEntity<>(this.userDetailService.createUser(authCreateUserRequest),HttpStatus.CREATED);
    }



    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        return new ResponseEntity<>(this.userDetailService.loginUser(userRequest), HttpStatus.OK);
    }
}
