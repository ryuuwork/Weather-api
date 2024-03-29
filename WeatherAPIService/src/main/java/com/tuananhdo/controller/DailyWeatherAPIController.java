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
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import payload.DailyWeatherDTO;
import payload.DailyWeatherListDTO;
import payload.LocationDTO;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        DailyWeatherListDTO dto = dailyWeatherMapper.mapToDailyWeatherListDTO(dailyWeatherList);
        return ResponseEntity.ok(addLinksByIPAddress(dto));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getDailyForecastByLocationCode(@PathVariable("code") String code) throws IOException {
        List<DailyWeather> dailyWeatherList = dailyWeatherService.getByLocationCode(code);
        if (dailyWeatherList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        DailyWeatherListDTO dto = dailyWeatherMapper.mapToDailyWeatherListDTO(dailyWeatherList);
        return ResponseEntity.ok(addLinksByIPAddress(dto));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateDailyForecastByLocationCode(@PathVariable("code") String code,
                                                               @RequestBody @Valid List<DailyWeatherDTO> dailyWeatherDTOS) throws BadRequestException {
        if (Objects.isNull(dailyWeatherDTOS) || dailyWeatherDTOS.isEmpty()) {
            throw new BadRequestException("The daily weather no data");
        }

        List<DailyWeather> dailyWeatherList = dailyWeatherMapper.mapToDailyWeatherList(dailyWeatherDTOS);
        List<DailyWeather> updateDailyWeather = dailyWeatherService.updateByLocationCode(code, dailyWeatherList);
        return ResponseEntity.ok(dailyWeatherMapper.mapToDailyWeatherListDTO(updateDailyWeather));
    }

    private EntityModel<DailyWeatherListDTO> addLinksByIPAddress(DailyWeatherListDTO dto) throws IOException {
       EntityModel<DailyWeatherListDTO> entityModel = EntityModel.of(dto);
        entityModel.add(linkTo(methodOn(DailyWeatherAPIController.class).getDailyForecastByIPAddess(null)).withRel("daily_forecast"));
        entityModel.add(linkTo(methodOn(HourlyWeatherAPIController.class).getHourlyWeatherByIPAddress(null)).withSelfRel());
        entityModel.add(linkTo(methodOn(RealtimeWeatherAPIController.class).getRealtimeWeatherByIPAddress(null)).withRel("realtime_forecast"));
        entityModel.add(linkTo(methodOn(FullWeatherAPIController.class).getFullWeatherByIPAddress(null)).withRel("full_forecast"));
        return entityModel;
    }
    private EntityModel<DailyWeatherListDTO> addLinksByLocationCode(DailyWeatherListDTO dto,String locationCode) throws IOException {
       EntityModel<DailyWeatherListDTO> entityModel = EntityModel.of(dto);
        entityModel.add(linkTo(methodOn(DailyWeatherAPIController.class).getDailyForecastByLocationCode(null)).withRel("daily_forecast"));
        entityModel.add(linkTo(methodOn(RealtimeWeatherAPIController.class).getRealtimeWeatherByLocationCode(locationCode)).withRel("realtime_forecast"));
        entityModel.add(linkTo(methodOn(HourlyWeatherAPIController.class).getHourlyForecastByLocationCode(locationCode,null)).withSelfRel());
        entityModel.add(linkTo(methodOn(FullWeatherAPIController.class).getFullWeatherByLocationCode(locationCode)).withRel("full_forecast"));
        return entityModel;
    }

}
