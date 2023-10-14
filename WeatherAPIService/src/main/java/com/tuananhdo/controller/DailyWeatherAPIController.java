package com.tuananhdo.controller;

import com.tuananhdo.entity.DailyWeather;
import com.tuananhdo.exception.BadRequestException;
import com.tuananhdo.exception.GeolocationException;
import com.tuananhdo.mapper.DailyWeatherMapper;
import com.tuananhdo.service.DailyWeatherService;
import com.tuananhdo.service.GeolocationService;
import com.tuananhdo.utils.CommonUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import payload.DailyWeatherDTO;
import payload.LocationDTO;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/daily")
@Validated
public class DailyWeatherAPIController {

    private final DailyWeatherService dailyWeatherService;
    private final GeolocationService geolocationService;
    private final DailyWeatherMapper dailyWeatherMapper;

    @GetMapping
    public ResponseEntity<?> getDailyForecastByIPAddess(HttpServletRequest request) throws GeolocationException, IOException {
        String ipAddess = CommonUtility.getIPAddress(request);
        LocationDTO locationDTO = geolocationService.getLocation(ipAddess);
        List<DailyWeather> dailyWeatherList = dailyWeatherService.getByLocation(locationDTO);
        if (dailyWeatherList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dailyWeatherMapper.mapToDailyWeatherListDTO(dailyWeatherList));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getDailyForecastByLocationCode(@PathVariable("code") String code) {
        List<DailyWeather> dailyWeatherList = dailyWeatherService.getByLocationCode(code);
        if (dailyWeatherList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dailyWeatherMapper.mapToDailyWeatherListDTO(dailyWeatherList));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateDailyForecastByLocationCode(@PathVariable("code") String code,
                                                               @RequestBody @Valid List<DailyWeatherDTO> dailyWeatherDTOS) throws BadRequestException {
        if (dailyWeatherDTOS.isEmpty()) {
            throw new BadRequestException("BAD REQUEST");
        }
        List<DailyWeather> dailyWeatherList = dailyWeatherMapper.mapToDailyWeatherList(dailyWeatherDTOS);
        List<DailyWeather> updateDailyWeather = dailyWeatherService.updateByLocationCode(code, dailyWeatherList);
        return ResponseEntity.ok(dailyWeatherMapper.mapToDailyWeatherListDTO(updateDailyWeather));
    }


}
