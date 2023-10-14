package com.tuananhdo.controller;

import com.tuananhdo.entity.HourlyWeather;
import com.tuananhdo.exception.BadRequestException;
import com.tuananhdo.exception.GeolocationException;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.mapper.HourlyWeatherMapper;
import com.tuananhdo.service.GeolocationService;
import com.tuananhdo.service.HourlyWeatherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import payload.HourlyWeatherDTO;
import payload.LocationDTO;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/hourly")
@Validated
public class HourlyWeatherAPIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HourlyWeatherAPIController.class);

    private final HourlyWeatherService hourlyWeatherService;
    private final GeolocationService geolocationService;
    private final HourlyWeatherMapper hourlyWeatherMapper;
    @GetMapping
    public ResponseEntity<?> getHourlyWeatherByIPAddress(HttpServletRequest request) {
        try {
//            String ipAddress = CommonUtility.getIPAddress(request);
            String ipAddress = "222.255.240.238";
            int currentHour = request.getIntHeader("X-Current-Hour");
            LocationDTO locationFromIP = geolocationService.getLocation(ipAddress);
            List<HourlyWeather> hourlyWeathers = hourlyWeatherService.getByLocation(locationFromIP, currentHour);
            if (hourlyWeathers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(hourlyWeatherMapper.mapHourlyWeatherListDTO(hourlyWeathers));
        } catch (NumberFormatException | GeolocationException | IOException exception) {
            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> hourlyForecastByLocationCode(@PathVariable("code") String locationCode, HttpServletRequest request) {
        int currentHour = request.getIntHeader("X-Current-Hour");
        List<HourlyWeather> hourlyWeather = hourlyWeatherService.getByLocationCode(locationCode, currentHour);
        if (hourlyWeather.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(hourlyWeatherMapper.mapHourlyWeatherListDTO(hourlyWeather));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateHourlyForecast(@PathVariable("code") String locationCode,
                                                  @RequestBody List<HourlyWeatherDTO> hourlyWeatherDTOS) throws BadRequestException {
        if (hourlyWeatherDTOS.isEmpty()) {
            throw new BadRequestException("Hourly forecast data cannot be empty");
        }
        try {
            List<HourlyWeather> hourlyWeatherList = hourlyWeatherMapper.mapToHourlyWeatherList(hourlyWeatherDTOS);
            List<HourlyWeather> updateHourlyWeather = hourlyWeatherService.updateByLocationCode(locationCode, hourlyWeatherList);
            return ResponseEntity.ok(hourlyWeatherMapper.mapHourlyWeatherListDTO(updateHourlyWeather));
        } catch (LocationNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
