package org.grupo1.markapbe.controller;

import org.grupo1.markapbe.controller.dto.UserDetailsResponse;
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

    @GetMapping("/{username}")
    public ResponseEntity<UserDetailsResponse> getUserProfile(@PathVariable String username) {
        return new ResponseEntity<>(userProfileService.getUserDetails(username), HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<UserDetailsResponse> updateUserProfile(@RequestBody UserDetailsResponse userDetailsResponse) {

    }



}
