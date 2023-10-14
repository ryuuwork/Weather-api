package com.tuananhdo.controller;

import com.tuananhdo.exception.BadRequestException;
import com.tuananhdo.service.FullWeatherService;
import com.tuananhdo.service.GeolocationService;
import com.tuananhdo.utils.CommonUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import payload.FullWeatherDTO;
import payload.LocationDTO;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/full")
@Validated
public class FullWeatherAPIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FullWeatherAPIController.class);

    private final GeolocationService geolocationService;
    private final FullWeatherService fullWeatherService;

    @GetMapping
    public ResponseEntity<FullWeatherDTO> getFullWeatherByIPAddress(HttpServletRequest request) throws IOException {
        String ipAddress = CommonUtility.getIPAddress(request);
        LocationDTO getIPLocation = geolocationService.getLocation(ipAddress);
        FullWeatherDTO getListFullWeatherByLocation = fullWeatherService.getByLocation(getIPLocation);
        return ResponseEntity.ok(getListFullWeatherByLocation);
    }

    @GetMapping("/{code}")
    public ResponseEntity<FullWeatherDTO> getFullWeatherByLocationCode(@PathVariable("code") String code) {
        FullWeatherDTO getListFullWeatherByLocationCode = fullWeatherService.getLocationByCode(code);
        return ResponseEntity.ok(getListFullWeatherByLocationCode);
    }

    @PutMapping("/{code}")
    public ResponseEntity<FullWeatherDTO> updateFullWeatherByLocationCode(@PathVariable("code") String code,
                                                                          @RequestBody @Valid FullWeatherDTO fullWeatherDTO) throws BadRequestException {
        if (fullWeatherDTO.getHourlyWeatherList().isEmpty()) {
            throw new BadRequestException("Hourly weather data cannot be empty");
        }
        if (fullWeatherDTO.getDailyWeatherList().isEmpty()) {
            throw new BadRequestException("Daily weather data cannot be empty");
        }
        return ResponseEntity.ok(fullWeatherService.updateFullWeatherByCode(code, fullWeatherDTO));
    }
}
