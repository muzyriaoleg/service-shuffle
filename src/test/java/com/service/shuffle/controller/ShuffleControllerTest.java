package com.service.shuffle.controller;

import com.service.shuffle.service.AsyncLoggingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShuffleController.class)
class ShuffleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestTemplate restTemplate;

    @MockitoBean
    private AsyncLoggingService asyncLoggingService;

    @ParameterizedTest
    @ValueSource(ints = {1, 1000, 100})
    void testShuffleValidNumber(int number) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/shuffle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(number)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(number));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 1001})
    void testShuffleInvalidNumberRange(int number) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/shuffle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(number)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "invalid request"})
    void testShuffleInvalidRequestBody(String invalidBody) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/shuffle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void testShuffleWithoutBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/shuffle")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }
}
