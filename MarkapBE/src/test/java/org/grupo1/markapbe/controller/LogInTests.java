package org.grupo1.markapbe.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.grupo1.markapbe.controller.dto.AuthLoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class LoginTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void IniciarSesion() throws Exception {
        String username = "master";
        String password = "4321";


        AuthLoginRequest request = new AuthLoginRequest(username, password);
        String requestJson = new ObjectMapper().writeValueAsString(request);


        mockMvc.perform(post("/auth/log-in").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isOk());
    }


    @Test
    void IniciarSesionUsuarioNoExiste() throws Exception {
        String username = "juanito";
        String password = "4321";


        AuthLoginRequest request = new AuthLoginRequest(username, password);
        String requestJson = new ObjectMapper().writeValueAsString(request);


        mockMvc.perform(post("/auth/log-in").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.errorMessage").value("El usuario " + username + " no existe"));
    }

    @Test
    void IniciarSesionContraseniaIncorrecta() throws Exception {
        String username = "master";
        String password = "noselacontrasenia";


        AuthLoginRequest request = new AuthLoginRequest(username, password);
        String requestJson = new ObjectMapper().writeValueAsString(request);


        mockMvc.perform(post("/auth/log-in").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.errorMessage").value("La contraseña es incorrecta."));
    }


}


