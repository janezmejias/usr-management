package com.usermanagement.core.infrastructure.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final String USR_REQUEST = """
            {
                "name": "juan",
                "email": "janezmejias.09@gmail.com",
                "password": "Test1234",
                "phones": [
                    {
                        "number": "3005650780",
                        "cityCode": "1",
                        "countryCode": "57"
                    }
                ]
            }
            """;

    @Test
    void signUpUserSuccess() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USR_REQUEST))
                .andExpect(status().isOk());
    }

    @Test
    void invalidToken() throws Exception {
        mockMvc.perform(get("/login")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid_token"))
                .andExpect(status().isForbidden());
    }

    @Test
    void signUpAndUseToken() throws Exception {
        var result = mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USR_REQUEST))
                .andExpect(status().isOk())
                .andReturn();

        var responseString = result.getResponse().getContentAsString();
        var responseJson = new JSONObject(responseString);
        var token = responseJson.getString("token");

        mockMvc.perform(get("/login")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

}