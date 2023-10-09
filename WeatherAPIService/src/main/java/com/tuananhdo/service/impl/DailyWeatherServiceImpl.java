package com.tuananhdo.service.impl;

import com.tuananhdo.entity.DailyWeather;
import com.tuananhdo.entity.Location;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.repository.DailyWeatherRepository;
import com.tuananhdo.repository.LocationRepository;
import com.tuananhdo.service.DailyWeatherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import payload.LocationDTO;

import java.util.List;

@Service
@AllArgsConstructor
public class DailyWeatherServiceImpl implements DailyWeatherService {
    private final DailyWeatherRepository dailyWeatherRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<DailyWeather> getByLocation(LocationDTO locationDTO) {
        String countryCode = locationDTO.getCountryCode();
        String cityName = locationDTO.getCityName();
        Location location = locationRepository.findByCountryCodeAndCityName(countryCode, cityName)
                .orElseThrow(() -> new LocationNotFoundException(countryCode, cityName));

        return dailyWeatherRepository.findByLocationCode(location.getCode());
    }
}
