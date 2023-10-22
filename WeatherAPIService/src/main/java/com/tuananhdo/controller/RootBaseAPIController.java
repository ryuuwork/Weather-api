package com.tuananhdo.controller;

import com.tuananhdo.utils.RootBaseURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
public class RootBaseAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootBaseAPIController.class);

    @GetMapping
    public ResponseEntity<RootBaseURL> handleBaseURL() throws IOException {
        return ResponseEntity.ok(createRootBaseURL());
    }

    private RootBaseURL createRootBaseURL() throws IOException {
        RootBaseURL rootBaseURL = new RootBaseURL();
        rootBaseURL.setLocationUrl(linkTo(methodOn(LocationAPIController.class).listAllUntrashedLocations()).toString());
        rootBaseURL.setLocationByCodeUrl(linkTo(methodOn(LocationAPIController.class).getLocation(null)).toString());

        rootBaseURL.setRealtimeWeatherByIpAdressUrl(linkTo(methodOn(RealtimeWeatherAPIController.class).getRealtimeWeatherByIPAddress(null)).toString());
        rootBaseURL.setRealtimeWeatherByCodeUrl(linkTo(methodOn(RealtimeWeatherAPIController.class).getRealtimeWeatherByLocationCode(null)).toString());

        rootBaseURL.setHourlyForecastByIpAddressUrl(linkTo(methodOn(HourlyWeatherAPIController.class).getHourlyWeatherByIPAddress(null)).toString());
        rootBaseURL.setHourlyForecastByCodeUrl(linkTo(methodOn(HourlyWeatherAPIController.class).getHourlyForecastByLocationCode(null, null)).toString());

        rootBaseURL.setDailyForecastByIpAddressUrl(linkTo(methodOn(DailyWeatherAPIController.class).getDailyForecastByIPAddess(null)).toString());
        rootBaseURL.setDailyForecastByCodeUrl(linkTo(methodOn(DailyWeatherAPIController.class).getDailyForecastByLocationCode(null)).toString());

        rootBaseURL.setFullForecastByIpAddressUrl(linkTo(methodOn(FullWeatherAPIController.class).getFullWeatherByIPAddress(null)).toString());
        rootBaseURL.setFullForecastByCodeUrl(linkTo(methodOn(FullWeatherAPIController.class).getFullWeatherByLocationCode(null)).toString());
        return rootBaseURL;
    }

}
