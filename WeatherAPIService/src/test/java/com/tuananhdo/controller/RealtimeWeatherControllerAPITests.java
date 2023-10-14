package com.tuananhdo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuananhdo.entity.Location;
import com.tuananhdo.entity.RealtimeWeather;
import com.tuananhdo.exception.GeolocationException;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.service.GeolocationService;
import com.tuananhdo.service.RealtimeWeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import payload.LocationDTO;
import payload.RealtimeWeatherDTO;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RealtimeWeatherAPIController.class)
public class RealtimeWeatherControllerAPITests {
    private static final String END_POINT_PATH = "/api/v1/realtime";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ModelMapper modelMapper;
    @MockBean
    RealtimeWeatherService realtimeWeatherService;
    @MockBean
    GeolocationService geolocationService;

    @Test
    public void testGetShouldReturnStatus400BadRequest() throws Exception {
        Mockito.when(geolocationService.getLocation(Mockito.anyString()))
                .thenThrow(GeolocationException.class);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    public void testGetShouldReturnStatus404NotFound() throws Exception {
        LocationDTO location = new LocationDTO();
        Mockito.when(geolocationService.getLocation(Mockito.anyString()))
                .thenReturn(location);

        Mockito.when(realtimeWeatherService.getByLocation(location))
                .thenThrow(LocationNotFoundException.class);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturnStatus200OK() throws Exception {
        Location location = new Location();
        location.setCode("VN_DBP");
        location.setCityName("Dien Bien");
        location.setRegionName("Dien Bien Phu");
        location.setCountryName("VN");
        location.setCountryCode("Viet Nam");
        location.setEnabled(true);

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(13);
        realtimeWeather.setHumidity(-10);
        realtimeWeather.setLastUpdated(LocalDateTime.now());
        realtimeWeather.setPrecipitation(20);
        realtimeWeather.setWindSpeed(60);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setLocation(location);
        location.setRealtimeWeather(realtimeWeather);

        LocationDTO locationDTO = modelMapper.map(location, LocationDTO.class);
        RealtimeWeatherDTO realtimeWeatherDTO = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);

        Mockito.when(geolocationService.getLocation(Mockito.anyString()))
                .thenReturn(locationDTO);
        Mockito.when(realtimeWeatherService.getByLocation(locationDTO))
                .thenReturn(realtimeWeatherDTO);

        String expectLocation = location.getCityName() + "," + location.getRegionName() + "," + location.getCountryName();
        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location", is(expectLocation)))
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequest() throws Exception {
        String locationCode = "AQWE_QWE";
        String URL = END_POINT_PATH + "/" + locationCode;

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(60);
        realtimeWeather.setHumidity(-10);
        realtimeWeather.setLastUpdated(LocalDateTime.now());
        realtimeWeather.setPrecipitation(20);
        realtimeWeather.setWindSpeed(60);
        realtimeWeather.setStatus("Cloudy");

        RealtimeWeatherDTO realtimeWeatherDTO = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
        String bodyContent = objectMapper.writeValueAsString(realtimeWeatherDTO);

        mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        String locationCode = "AQWE_QWE";
        String URL = END_POINT_PATH + "/" + locationCode;

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(30);
        realtimeWeather.setHumidity(20);
        realtimeWeather.setLastUpdated(LocalDateTime.now());
        realtimeWeather.setPrecipitation(20);
        realtimeWeather.setWindSpeed(60);
        realtimeWeather.setStatus("Cloudy");

        RealtimeWeatherDTO realtimeWeatherDTO = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);

        Mockito.when(realtimeWeatherService.updateRealtimeWeather(locationCode, realtimeWeatherDTO))
                .thenThrow(LocationNotFoundException.class);

        String bodyContent = objectMapper.writeValueAsString(realtimeWeatherDTO);

        mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        String locationCode = "BER";
        String URL = END_POINT_PATH + "/" + locationCode;

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(30);
        realtimeWeather.setHumidity(20);
        realtimeWeather.setPrecipitation(20);
        realtimeWeather.setWindSpeed(60);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setLastUpdated(LocalDateTime.now());

        Location location = new Location();
        location.setCode(locationCode);
        location.setCityName("Berlin");
        location.setRegionName("Germany Star Of");
        location.setCountryName("GE");
        location.setCountryCode("Germany");
        location.setEnabled(true);

        realtimeWeather.setLocation(location);
        location.setRealtimeWeather(realtimeWeather);

        RealtimeWeatherDTO realtimeWeatherDTO = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
        LocationDTO locationDTO = modelMapper.map(location, LocationDTO.class);

        Mockito.when(realtimeWeatherService.updateRealtimeWeather(locationDTO.getCode(), realtimeWeatherDTO))
                .thenReturn(realtimeWeatherDTO);

        String bodyContent = objectMapper.writeValueAsString(realtimeWeatherDTO);
        String expectLocation = location.getCityName() + "," + location.getRegionName() + "," + location.getCountryName();

        mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location", is(expectLocation)))
                .andDo(print());
    }
}
