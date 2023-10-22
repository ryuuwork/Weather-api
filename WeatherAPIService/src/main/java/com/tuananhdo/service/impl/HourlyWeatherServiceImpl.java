package com.tuananhdo.service.impl;

import com.tuananhdo.entity.HourlyWeather;
import com.tuananhdo.entity.Location;
import com.tuananhdo.repository.HourlyWeatherRepository;
import com.tuananhdo.repository.LocationRepository;
import com.tuananhdo.service.AbstractLocationSerivce;
import com.tuananhdo.service.HourlyWeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import payload.LocationDTO;

import java.util.List;

@Service
public class HourlyWeatherServiceImpl extends AbstractLocationSerivce implements HourlyWeatherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HourlyWeatherServiceImpl.class);
    private final HourlyWeatherRepository hourlyWeatherRepository;
    public HourlyWeatherServiceImpl(LocationRepository locationRepository, HourlyWeatherRepository hourlyWeatherRepository) {
        this.locationRepository = locationRepository;
        this.hourlyWeatherRepository = hourlyWeatherRepository;
    }

    @Override
    public List<HourlyWeather> findByLocationAndHour(LocationDTO locationDTO, int currentHour) {
        Location location = getCountryCodeAndCityName(locationDTO);
        return hourlyWeatherRepository.findByLocationCodeAndHour(location.getCode(), currentHour);
    }

    @Override
    public List<HourlyWeather> getByLocationCode(String code, int currentHour) {
        Location location = getLocationCode(code);
        return hourlyWeatherRepository.findByLocationCodeAndHour(location.getCode(), currentHour);
    }

    @Override
    public List<HourlyWeather> updateByLocationCode(String code,
                                                    List<HourlyWeather> hourlyWeathers) {
        Location location = getLocationCode(code);
        hourlyWeathers.forEach(item -> item.getWeatherId().setLocation(location));
        LOGGER.info("Location = {}", location);
        return hourlyWeatherRepository.saveAll(hourlyWeathers);
    }

    @Override
    public List<HourlyWeather> getByLocation(LocationDTO locationDTO, int currentHour) {
        Location location = getCountryCodeAndCityName(locationDTO);
        return hourlyWeatherRepository.findByLocationCodeAndHour(location.getCode(), currentHour);
    }

}
