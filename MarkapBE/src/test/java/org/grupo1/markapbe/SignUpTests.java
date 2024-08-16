package org.grupo1.markapbe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.grupo1.markapbe.controller.dto.AuthCreateUserRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class SignUpTests {

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
    }

    @Test
    void RegistrarContraseniaVacia() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("TomasP", "", "Tomas Perez", "tperez@mail.com");
        String requestJson = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorMessage").value("La contraseña debe tener minimo 3 caracteres."));

    }


    @Test
    void RegistrarEmailInvalido() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("TomasP", "1221", "Tomas Perez", "tperezmail.com");
        String requestJson = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorMessage").value("Debes ingresar un email valido."));
    }



    @Test
    void RegistrarFeliz() throws Exception {
        AuthCreateUserRequest request = new AuthCreateUserRequest("TomasP", "1221", "Tomas Perez", "tperez@mail.com");
        String requestJson = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.jwt").isNotEmpty());
    }

}

