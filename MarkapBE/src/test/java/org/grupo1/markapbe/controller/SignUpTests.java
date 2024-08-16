package org.grupo1.markapbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.grupo1.markapbe.controller.dto.AuthCreateUserRequest;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
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
class SignUpTests {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void RegistrarUsuarioVacio() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("", "1221", "Tomas Perez", "tperez@mail.com");
        String requestJson = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorMessage").value("El nombre de usuario no puede estar vacio."));

        Assertions.assertTrue(userRepository.findUserEntityByUsername("").isEmpty());
    }

    @Test
    void RegistrarContraseniaVacia() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("TomasP", "", "Tomas Perez", "tperez@mail.com");
        String requestJson = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorMessage").value("La contraseña debe tener minimo 3 caracteres."));
        Assertions.assertTrue(userRepository.findUserEntityByUsername("TomasP").isEmpty());

    }


    @Test
    void RegistrarEmailInvalido() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("TomasP", "1221", "Tomas Perez", "tperezmail.com");
        String requestJson = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorMessage").value("Debes ingresar un email valido."));

        Assertions.assertTrue(userRepository.findUserEntityByUsername("TomasP").isEmpty());
    }


    @Test
    void RegistrarFeliz() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("tomyperezboca01", "1221", "Tomas Perez", "tomy_perez@mail.com");
        String requestJson = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.jwt").isNotEmpty());

        Assertions.assertFalse(userRepository.findUserEntityByUsername("tomyperezboca01").isEmpty());


    }

}

