package org.grupo1.markapbe.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.grupo1.markapbe.controller.dto.AuthCreateUserRequest;
import org.grupo1.markapbe.controller.dto.AuthLoginRequest;
import org.grupo1.markapbe.controller.dto.ErrorResponseDTO;
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
    public ResponseEntity<?> register(@RequestBody @Valid AuthCreateUserRequest authCreateUserRequest){

        try {
        return new ResponseEntity<>(this.userDetailService.createUser(authCreateUserRequest),HttpStatus.CREATED);}
        catch (Exception ex) {
            return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()),HttpStatus.UNAUTHORIZED);
        }
    }



    @PostMapping("/log-in")
    public ResponseEntity<?> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        try {
        return new ResponseEntity<>(this.userDetailService.loginUser(userRequest), HttpStatus.OK);}
        catch (Exception ex) {
            return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()),HttpStatus.UNAUTHORIZED);
        }
    }
}
