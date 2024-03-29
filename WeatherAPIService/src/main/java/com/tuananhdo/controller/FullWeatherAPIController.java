package com.tuananhdo.controller;

import com.tuananhdo.exception.BadRequestException;
import com.tuananhdo.service.FullWeatherService;
import com.tuananhdo.service.GeolocationService;
import com.tuananhdo.utils.CommonUtility;
import com.tuananhdo.utils.FullWeatherModelAssembler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import payload.FullWeatherDTO;
import payload.LocationDTO;

import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/full")
public class FullWeatherAPIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FullWeatherAPIController.class);

    private final GeolocationService geolocationService;
    private final FullWeatherService fullWeatherService;
    private final FullWeatherModelAssembler modelAssembler;

    @GetMapping
    public ResponseEntity<?> getFullWeatherByIPAddress(HttpServletRequest request) throws IOException {
        String ipAddress = CommonUtility.getIPAddress(request);
        LocationDTO getIPLocation = geolocationService.getLocation(ipAddress);
        FullWeatherDTO dto = fullWeatherService.getByLocation(getIPLocation);
        return ResponseEntity.ok(modelAssembler.toModel(dto));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getFullWeatherByLocationCode(@PathVariable("code") String code) {
        FullWeatherDTO dto = fullWeatherService.getLocationByCode(code);
        return ResponseEntity.ok(addLinkByLocation(dto,code));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateFullWeatherByLocationCode(@PathVariable("code") String code,
                                                                          @RequestBody @Valid FullWeatherDTO fullWeatherDTO) throws BadRequestException {
        if (fullWeatherDTO.getHourlyWeatherList().isEmpty()) {
            throw new BadRequestException("Hourly weather data cannot be empty");
        }
        if (fullWeatherDTO.getDailyWeatherList().isEmpty()) {
            throw new BadRequestException("Daily weather data cannot be empty");
        }
        return ResponseEntity.ok(fullWeatherService.updateFullWeatherByCode(code, fullWeatherDTO));
    }

    private EntityModel<FullWeatherDTO> addLinkByLocation(FullWeatherDTO fullWeatherDTO,String locationCode){
        return EntityModel.of(fullWeatherDTO)
                .add(linkTo(methodOn(FullWeatherAPIController.class)
                        .getFullWeatherByLocationCode(locationCode))
                        .withSelfRel());
    }
}
