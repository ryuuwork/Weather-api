package com.tuananhdo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuananhdo.entity.HourlyWeather;
import com.tuananhdo.entity.Location;
import com.tuananhdo.exception.GeolocationException;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.mapper.HourlyWeatherMapper;
import com.tuananhdo.service.GeolocationService;
import com.tuananhdo.service.HourlyWeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import payload.HourlyWeatherDTO;
import payload.LocationDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HourlyWeatherAPIController.class)
public class HourlyWeatherControllerAPITests {
    private static final Logger LOGGER = LoggerFactory.getLogger(HourlyWeatherControllerAPITests.class);
    private static final String END_POINT_PATH = "/api/v1/hourly";
    public static final String X_CURRENT_HOUR = "X-Current-Hour";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private HourlyWeatherService hourlyWeatherService;
    @MockBean
    private GeolocationService geolocationService;
    @MockBean
    private HourlyWeatherMapper hourlyWeatherMapper;
    @Autowired
    ModelMapper mapper;

    @Test
    public void testMissingXCurrentHourHeader_return400BadRequest() throws Exception {
        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, ""))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testAddXCurrentHourHeader_return400BadRequest_BecauseGeolocationException() throws Exception {
        when(geolocationService.getLocation(Mockito.anyString()))
                .thenThrow(GeolocationException.class);

        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, "22"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGetIPAddress_Return204NoContent() throws Exception {
        int currentHour = 9;
        LocationDTO location = new LocationDTO()
                .code("VN_DBP");
        when(geolocationService.getLocation(Mockito.anyString()))
                .thenReturn(location);
        when(hourlyWeatherService.findByLocationAndHour(location, currentHour))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get(END_POINT_PATH)
                        .header(X_CURRENT_HOUR, String.valueOf(currentHour)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


    @Test
    public void testGetIPAddress_Return200OK() throws Exception {
        int currentHour = 9;
        Location location = new Location();
        location.setCode("VN_DBP");
        location.setCityName("Dien Bien");
        location.setRegionName("Dien Bien Phu");
        location.setCountryName("VN");
        location.setCountryCode("Viet Nam");
        location.setEnabled(true);

        HourlyWeather hourlyWeather = new HourlyWeather()
                .location(location)
                .hourOfDay(13)
                .temperature(20)
                .precipitation(40)
                .status("Rain");

        HourlyWeather hourlyWeather2 = new HourlyWeather()
                .location(location)
                .hourOfDay(12)
                .temperature(30)
                .precipitation(20)
                .status("Rain2");

        LocationDTO locationDTO = mapper.map(location, LocationDTO.class);

        List<HourlyWeather> hourlyWeathers = List.of(hourlyWeather, hourlyWeather2);

        when(geolocationService.getLocation(Mockito.anyString())).thenReturn(locationDTO);
        when(hourlyWeatherService.getByLocation(locationDTO, currentHour)).thenReturn(hourlyWeathers);
        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequestBecauseNoData() throws Exception {
        String requestURI = END_POINT_PATH + "/VN_DBP";
        List<HourlyWeatherDTO> hourlyWeatherDTOS = Collections.emptyList();
        String requestBody = objectMapper.writeValueAsString(hourlyWeatherDTOS);
        mockMvc.perform(put(requestURI).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn404NotFound() throws Exception {
        int currentHour = 9;
        String locationCode = "VN_DBP";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        LocationNotFoundException exception = new LocationNotFoundException(locationCode);
        when(hourlyWeatherService.getByLocationCode(locationCode, currentHour)).thenThrow(exception);

        mockMvc.perform(get(requestURI).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]", is(exception.getMessage())))
                .andDo(print());
    }
}
