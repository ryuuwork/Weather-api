package com.tuananhdo.controller;

import com.tuananhdo.entity.DailyWeather;
import com.tuananhdo.entity.HourlyWeather;
import com.tuananhdo.entity.Location;
import com.tuananhdo.entity.RealtimeWeather;
import com.tuananhdo.exception.GeolocationException;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.service.FullWeatherService;
import com.tuananhdo.service.GeolocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import payload.LocationDTO;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FullWeatherAPIController.class)
public class FullWeatherControllerAPITests {
    private static final Logger LOGGER = LoggerFactory.getLogger(FullWeatherControllerAPITests.class);
    private static final String END_POINT_PATH = "/api/v1/full";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FullWeatherService fullWeatherService;
    @MockBean
    private GeolocationService geolocationService;
    @Autowired
    private ModelMapper mapper;

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
        when(fullWeatherService.getByLocation(locationDTO)).thenThrow(locationNotFoundException);
        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]", is(locationNotFoundException.getMessage())))
                .andDo(print());
    }

    @Test
    public void testGetIPAddress_Return200OK() throws Exception {
        Location location = new Location();
        location.setCode("VN_DBP");
        location.setCityName("Dien Bien");
        location.setRegionName("Dien Bien Phu");
        location.setCountryName("VN");
        location.setCountryCode("Viet Nam");
        location.setEnabled(true);

        RealtimeWeather realtimeWeather = new RealtimeWeather()
                .temperature(12)
                .humidity(32)
                .lastUpdated(LocalDateTime.now())
                .precipitation(22)
                .status("Rain")
                .windSpeed(11);

        location.setRealtimeWeather(realtimeWeather);

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

        location.setHourlyWeatherList(List.of(hourlyWeather, hourlyWeather2));

        DailyWeather dailyWeather = new DailyWeather()
                .location(location)
                .dayOfMonth(11)
                .month(2)
                .minTemp(23)
                .maxTemp(23)
                .precipitation(22)
                .status("Sunny");

        DailyWeather dailyWeather2 = new DailyWeather()
                .location(location)
                .dayOfMonth(13)
                .month(3)
                .minTemp(23)
                .maxTemp(23)
                .precipitation(22)
                .status("Cloudy");

        location.setDailyWeatherList(List.of(dailyWeather, dailyWeather2));

        LocationDTO locationDTO = mapper.map(location, LocationDTO.class);

        when(geolocationService.getLocation(Mockito.anyString())).thenReturn(locationDTO);
//        when(fullWeatherService.getByLocation(locationDTO)).thenReturn(locationDTO);
        String expectedLocation = locationDTO.toString();
        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location",is(expectedLocation)))
                .andDo(print());
    }

}
