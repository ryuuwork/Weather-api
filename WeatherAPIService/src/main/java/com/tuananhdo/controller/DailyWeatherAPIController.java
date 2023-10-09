package com.tuananhdo.controller;

import com.tuananhdo.entity.DailyWeather;
import com.tuananhdo.exception.GeolocationException;
import com.tuananhdo.mapper.DailyWeatherMapper;
import com.tuananhdo.service.DailyWeatherService;
import com.tuananhdo.service.GeolocationService;
import com.tuananhdo.utils.CommonUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import payload.LocationDTO;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/daily")
public class DailyWeatherAPIController {

    private final DailyWeatherService dailyWeatherService;
    private final GeolocationService geolocationService;
    private final DailyWeatherMapper dailyWeatherMapper;
    private final ModelMapper mapper; //    If converted to modalmapper , test passes

    @GetMapping
    public ResponseEntity<?> getDailyForecastByIPAddess(HttpServletRequest request) throws GeolocationException, IOException {
        String ipAddess = CommonUtility.getIPAddress(request);
        LocationDTO locationDTO = geolocationService.getLocation(ipAddess);
        List<DailyWeather> dailyWeatherList = dailyWeatherService.getByLocation(locationDTO);
        if (dailyWeatherList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dailyWeatherMapper.mapDailyWeatherListDTO(dailyWeatherList));
    }


}
