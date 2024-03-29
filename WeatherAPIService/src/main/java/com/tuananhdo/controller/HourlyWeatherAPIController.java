package com.tuananhdo.controller;

import com.tuananhdo.entity.HourlyWeather;
import com.tuananhdo.exception.BadRequestException;
import com.tuananhdo.exception.GeolocationException;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.mapper.HourlyWeatherMapper;
import com.tuananhdo.service.GeolocationService;
import com.tuananhdo.service.HourlyWeatherService;
import com.tuananhdo.utils.CommonUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import payload.HourlyWeatherDTO;
import payload.HourlyWeatherListDTO;
import payload.LocationDTO;

import java.io.IOException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
            String ipAddress = CommonUtility.getIPAddress(request);
            int currentHour = request.getIntHeader("X-Current-Hour");
            LocationDTO locationFromIP = geolocationService.getLocation(ipAddress);
            List<HourlyWeather> hourlyWeathers = hourlyWeatherService.getByLocation(locationFromIP, currentHour);
            if (hourlyWeathers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            HourlyWeatherListDTO dto = hourlyWeatherMapper.mapHourlyWeatherListDTO(hourlyWeathers);
            return ResponseEntity.ok(addLinksByIPAddress(dto));
        } catch (NumberFormatException | GeolocationException | IOException exception) {
            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getHourlyForecastByLocationCode(@PathVariable("code") String locationCode, HttpServletRequest request) throws IOException {
        int currentHour = request.getIntHeader("X-Current-Hour");
        List<HourlyWeather> hourlyWeather = hourlyWeatherService.getByLocationCode(locationCode, currentHour);
        if (hourlyWeather.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        HourlyWeatherListDTO dto = hourlyWeatherMapper.mapHourlyWeatherListDTO(hourlyWeather);
        return ResponseEntity.ok(addLinksByLocationCode(dto,locationCode));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateHourlyForecast(@PathVariable("code") String locationCode,
                                                  @RequestBody List<HourlyWeatherDTO> hourlyWeatherDTOS) throws BadRequestException, IOException {
        if (hourlyWeatherDTOS.isEmpty()) {
            throw new BadRequestException("Hourly forecast data cannot be empty");
        }
        List<HourlyWeather> hourlyWeatherList = hourlyWeatherMapper.mapToHourlyWeatherList(hourlyWeatherDTOS);
        List<HourlyWeather> updateHourlyWeather = hourlyWeatherService.updateByLocationCode(locationCode, hourlyWeatherList);

        HourlyWeatherListDTO updatedDTO = hourlyWeatherMapper.mapHourlyWeatherListDTO(updateHourlyWeather);

        return ResponseEntity.ok(addLinksByLocationCode(updatedDTO, locationCode));
    }

    private HourlyWeatherListDTO addLinksByIPAddress(HourlyWeatherListDTO dto) throws IOException {
        dto.add(linkTo(methodOn(HourlyWeatherAPIController.class).getHourlyWeatherByIPAddress(null)).withSelfRel());
        dto.add(linkTo(methodOn(RealtimeWeatherAPIController.class).getRealtimeWeatherByIPAddress(null)).withRel("realtime_forecast"));
        dto.add(linkTo(methodOn(DailyWeatherAPIController.class).getDailyForecastByIPAddess(null)).withRel("daily_forecast"));
        dto.add(linkTo(methodOn(FullWeatherAPIController.class).getFullWeatherByIPAddress(null)).withRel("full_forecast"));
        return dto;
    }

    private HourlyWeatherListDTO addLinksByLocationCode(HourlyWeatherListDTO dto,String code) throws IOException {
        dto.add(linkTo(methodOn(HourlyWeatherAPIController.class).getHourlyForecastByLocationCode(code,null)).withSelfRel());
        dto.add(linkTo(methodOn(RealtimeWeatherAPIController.class).getRealtimeWeatherByLocationCode(code)).withRel("realtime_forecast"));
        dto.add(linkTo(methodOn(DailyWeatherAPIController.class).getDailyForecastByLocationCode(code)).withRel("daily_forecast"));
        dto.add(linkTo(methodOn(FullWeatherAPIController.class).getFullWeatherByLocationCode(code)).withRel("full_forecast"));
        return dto;
    }
}
