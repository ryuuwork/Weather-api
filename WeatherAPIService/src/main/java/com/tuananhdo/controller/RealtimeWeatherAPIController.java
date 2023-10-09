package com.tuananhdo.controller;

import com.tuananhdo.exception.GeolocationException;
import com.tuananhdo.service.GeolocationService;
import com.tuananhdo.service.RealtimeWeatherService;
import com.tuananhdo.utils.CommonUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payload.LocationDTO;
import payload.RealtimeWeatherDTO;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/realtime")
@AllArgsConstructor
public class RealtimeWeatherAPIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherAPIController.class);

    private final GeolocationService geolocationService;
    private final RealtimeWeatherService realtimeWeatherService;
    @GetMapping
    public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) throws GeolocationException, IOException {
        String ipAddress = CommonUtility.getIPAddress(request);
        LocationDTO locationFromIP = geolocationService.getLocation(ipAddress);
        RealtimeWeatherDTO realtimeWeather = realtimeWeatherService.getByLocation(locationFromIP);
        return ResponseEntity.ok(realtimeWeather);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("code") String code) {
        RealtimeWeatherDTO realtimeWeather = realtimeWeatherService.getByLocationCode(code);
        return ResponseEntity.ok(realtimeWeather);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateRealtimeWeather(@PathVariable("code") String code,
                                                   @RequestBody @Valid RealtimeWeatherDTO realtimeWeatherDTO) {
        RealtimeWeatherDTO realtimeWeather = realtimeWeatherService.updateRealtimeWeather(code, realtimeWeatherDTO);
        LOGGER.error(String.valueOf(realtimeWeather));
        return ResponseEntity.ok(realtimeWeather);
    }
}
