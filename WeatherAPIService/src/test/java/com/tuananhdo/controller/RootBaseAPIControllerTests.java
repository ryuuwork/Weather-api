package com.tuananhdo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RootBaseAPIController.class)
public class RootBaseAPIControllerTests {

    public static final String BASE_URL = "/";
    @Autowired
    private MockMvc mockMvc;




    @Test
    public void testBaseURL() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.locationUrl", is("http://localhost/api/v1/locations")))
                .andExpect(jsonPath("$.locationByCodeUrl", is("http://localhost/api/v1/locations/{code}")))
                .andExpect(jsonPath("$.realtimeWeatherByIPAdressUrl", is("http://localhost/api/v1/realtime")))
                .andDo(print());
    }
}
