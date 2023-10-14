package com.tuananhdo.controller;

import com.tuananhdo.exception.GeolocationException;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.mapper.DailyWeatherMapper;
import com.tuananhdo.service.DailyWeatherService;
import com.tuananhdo.service.GeolocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import payload.LocationDTO;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyWeatherAPIController.class)
public class DailyWeatherControllerAPITests {
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyWeatherControllerAPITests.class);
    private static final String END_POINT_PATH = "/api/v1/daily";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DailyWeatherService dailyWeatherService;
    @MockBean
    private GeolocationService geolocationService;
    @MockBean
    private DailyWeatherMapper dailyWeatherMapper;

    @Test
    public void testGetByIPAddressShouldReturn400BadRequestBecauseGeolocationException() throws Exception {
        GeolocationException geolocationException = new GeolocationException("geolocationException");
        when(geolocationService.getLocation(Mockito.anyString())).thenThrow(geolocationException);
        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]", is(geolocationException.getMessage())))
                .andDo(print());
    }

    @Test
    public void testGetByIPAddressShouldReturn404NotFound() throws Exception {
        LocationDTO locationDTO = new LocationDTO().code("THAI");
        when(geolocationService.getLocation(Mockito.anyString())).thenReturn(locationDTO);
        LocationNotFoundException locationNotFoundException = new LocationNotFoundException(locationDTO.getCode());
        when(dailyWeatherService.getByLocation(locationDTO)).thenThrow(locationNotFoundException);
        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]", is(locationNotFoundException.getMessage())))
                .andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn204NoContent() throws Exception {
        String code = "VN_DBPS";
        String requestURL = END_POINT_PATH + "/" + code;
        when(dailyWeatherService.getByLocationCode(code)).thenReturn(new ArrayList<>());
        mockMvc.perform(get(requestURL))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


}
