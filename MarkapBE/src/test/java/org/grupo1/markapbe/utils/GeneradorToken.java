package org.grupo1.markapbe.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.grupo1.markapbe.controller.dto.AuthLoginRequest;
import org.grupo1.markapbe.controller.dto.AuthResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class GeneradorToken {
    private final MockMvc mockMvc;


    public GeneradorToken(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public String loginAndGetJwtToken(String username, String password) throws Exception {
        AuthLoginRequest request = new AuthLoginRequest(username, password);
        String requestJson = new ObjectMapper().writeValueAsString(request);


        MvcResult result = mockMvc.perform(post("/auth/log-in").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isOk()).andReturn();
        String responseBody = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        AuthResponse authResponse = objectMapper.readValue(responseBody, AuthResponse.class);

        return authResponse.jwt();
    }
}
