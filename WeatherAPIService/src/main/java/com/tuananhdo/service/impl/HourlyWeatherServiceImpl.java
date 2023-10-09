package com.tuananhdo.service.impl;

import com.tuananhdo.entity.HourlyWeather;
import com.tuananhdo.entity.Location;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.mapper.HourlyWeatherMapper;
import com.tuananhdo.mapper.LocationMapper;
import com.tuananhdo.repository.HourlyWeatherRepository;
import com.tuananhdo.repository.LocationRepository;
import com.tuananhdo.service.HourlyWeatherService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import payload.HourlyWeatherDTO;
import payload.LocationDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HourlyWeatherServiceImpl implements HourlyWeatherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HourlyWeatherServiceImpl.class);

    private final LocationRepository locationRepository;
    private final HourlyWeatherRepository hourlyWeatherRepository;
    private final HourlyWeatherMapper hourlyWeatherMapper;
    private final LocationMapper locationMapper;

    @Override
    public List<HourlyWeather> findByLocationAndHour(LocationDTO location, int currentHour) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        Location locationIP = locationRepository.findByCountryCodeAndCityName(countryCode, cityName)
                .orElseThrow(() -> new LocationNotFoundException(countryCode, cityName));
        LocationDTO locationDTO = locationMapper.mapToLocationDTO(locationIP);
        return hourlyWeatherRepository.findByLocationCodeAndHour(locationDTO.getCode(), currentHour);
    }

    @Override
    public List<HourlyWeatherDTO> getByLocationCode(String locationCode, int currentHour) {
        locationRepository.findByCode(locationCode)
                .orElseThrow(() -> new LocationNotFoundException(locationCode));
        List<HourlyWeather> hourlyWeathers = hourlyWeatherRepository.findByLocationCodeAndHour(locationCode, currentHour);
        return hourlyWeathers
                .stream()
                .map(hourlyWeatherMapper::mapToHourlyWeatherDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HourlyWeather> updateByLocationCode(String locationCode,
                                                    List<HourlyWeather> hourlyWeathers) {
        Location location = locationRepository.findByCode(locationCode).orElseThrow(() ->
                new LocationNotFoundException(locationCode));
        hourlyWeathers.forEach(item -> item.getWeatherId().setLocation(location));
        return hourlyWeatherRepository.saveAll(hourlyWeathers);
    }
}
