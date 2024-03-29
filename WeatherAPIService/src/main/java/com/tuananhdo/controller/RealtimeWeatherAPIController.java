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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        RealtimeWeatherDTO dto = realtimeWeatherService.getByLocation(locationFromIP);
        return ResponseEntity.ok(addLinksByIPAddress(dto));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("code") String code) throws IOException {
        RealtimeWeatherDTO realtimeWeather = realtimeWeatherService.getByLocationCode(code);
        return ResponseEntity.ok(addLinksByLocation(realtimeWeather,code));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateRealtimeWeather(@PathVariable("code") String code,
                                                   @RequestBody @Valid RealtimeWeatherDTO dto) throws IOException {
        RealtimeWeatherDTO realtimeWeather = realtimeWeatherService.updateRealtimeWeather(code, dto);
        return ResponseEntity.ok(addLinksByLocation(realtimeWeather,code));
    }

    private RealtimeWeatherDTO addLinksByIPAddress(RealtimeWeatherDTO dto) throws IOException {
        dto.add(linkTo(methodOn(RealtimeWeatherAPIController.class).getRealtimeWeatherByIPAddress(null)).withSelfRel());
        dto.add(linkTo(methodOn(HourlyWeatherAPIController.class).getHourlyWeatherByIPAddress(null)).withRel("hourly_forecast"));
        dto.add(linkTo(methodOn(DailyWeatherAPIController.class).getDailyForecastByIPAddess(null)).withRel("daily_forecast"));
        dto.add(linkTo(methodOn(FullWeatherAPIController.class).getFullWeatherByIPAddress(null)).withRel("full_forecast"));
        return dto;
    }

    private RealtimeWeatherDTO addLinksByLocation(RealtimeWeatherDTO dto,String code) throws IOException {
        dto.add(linkTo(methodOn(RealtimeWeatherAPIController.class).getRealtimeWeatherByLocationCode(code)).withSelfRel());
        dto.add(linkTo(methodOn(HourlyWeatherAPIController.class).getHourlyForecastByLocationCode(code,null)).withRel("hourly_forecast"));
        dto.add(linkTo(methodOn(DailyWeatherAPIController.class).getDailyForecastByLocationCode(code)).withRel("daily_forecast"));
        dto.add(linkTo(methodOn(FullWeatherAPIController.class).getFullWeatherByLocationCode(code)).withRel("full_forecast"));
        return dto;
    }
}
