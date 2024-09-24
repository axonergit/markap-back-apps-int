package org.grupo1.markapbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.grupo1.markapbe.controller.dto.AuthDTO.AuthCreateUserRequest;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ProductsTests {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    public ProductsTests(){
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void RegistrarUsuarioVacio() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("", "1221", "Tomas", "Perez","tperez@mail.com", LocalDate.of(2004,5,10));
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorMessage").value("El nombre de usuario no puede estar vacio."));

        Assertions.assertTrue(userRepository.findUserEntityByUsername("").isEmpty());
    }

    @Test
    void RegistrarContraseniaVacia() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("TomasP", "", "Tomas", "Perez","tperez@mail.com", LocalDate.of(2004,5,10));
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorMessage").value("La contraseña no puede estar vacía."));
        Assertions.assertTrue(userRepository.findUserEntityByUsername("TomasP").isEmpty());

    }

    @Test
    void RegistrarContraseniaCorta() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("TomasP", "12", "Tomas", "Perez","tperez@mail.com", LocalDate.of(2004,5,10));
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorMessage").value("La contraseña debe tener minimo 3 caracteres."));
        Assertions.assertTrue(userRepository.findUserEntityByUsername("TomasP").isEmpty());

    }




    @Test
    void RegistrarNombreVacio() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("TomasP", "1234", "", "Perez","tperez@mail.com", LocalDate.of(2004,5,10));
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorMessage").value("El nombre no puede estar vacio"));
        Assertions.assertTrue(userRepository.findUserEntityByUsername("TomasP").isEmpty());

    }


    @Test
    void RegistrarApellidoVacio() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("TomasP", "1234", "Tomas", "","tperez@mail.com", LocalDate.of(2004,5,10));
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorMessage").value("El apellido no puede estar vacio"));
        Assertions.assertTrue(userRepository.findUserEntityByUsername("TomasP").isEmpty());

    }


    @Test
    void RegistrarEmailInvalido() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("TomasP", "1221", "Tomas", "Perez","tperezmail.com", LocalDate.of(2004,5,10));
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorMessage").value("Debes ingresar un email valido."));

        Assertions.assertTrue(userRepository.findUserEntityByUsername("TomasP").isEmpty());
    }

    @Test
    void RegistrarFechaFutura() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("TomasP", "1221", "Tomas", "Perez","tperez@mail.com", LocalDate.of(2025,5,10));
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorMessage").value("La fecha de nacimiento tiene que ser en el pasado"));

        Assertions.assertTrue(userRepository.findUserEntityByUsername("TomasP").isEmpty());
    }


    @Test
    void RegistrarFechaVacia() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("TomasP", "1221", "Tomas", "Perez","tperez@mail.com", null);
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        Assertions.assertTrue(userRepository.findUserEntityByUsername("TomasP").isEmpty());
    }



    @Test
    void RegistrarFeliz() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("TomasPerez", "1221", "Tomas", "Perez","tperez@mail.com", LocalDate.of(2004,5,10));
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.jwt").isNotEmpty());

        Assertions.assertFalse(userRepository.findUserEntityByUsername("TomasPerez").isEmpty());


    }

}

