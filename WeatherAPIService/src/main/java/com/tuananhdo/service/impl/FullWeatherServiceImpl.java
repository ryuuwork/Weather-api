package com.tuananhdo.service.impl;

import com.tuananhdo.entity.DailyWeather;
import com.tuananhdo.entity.HourlyWeather;
import com.tuananhdo.entity.Location;
import com.tuananhdo.entity.RealtimeWeather;
import com.tuananhdo.mapper.FullWeatherMapper;
import com.tuananhdo.service.FullWeatherService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import payload.FullWeatherDTO;
import payload.LocationDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class FullWeatherServiceImpl extends AbstractLocationSerivce implements FullWeatherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeolocationServiceImpl.class);
    private final FullWeatherMapper fullWeatherMapper;

    @Override
    public FullWeatherDTO getByLocation(LocationDTO locationDTO) {
        Location location = getCountryCodeAndCityName(locationDTO);
        LOGGER.info("Location countryCode={}, city={}", location.getCountryCode(), location.getCityName());
        return fullWeatherMapper.mapLocationToFullWeatherDTO(location);
    }


    @Override
    public FullWeatherDTO getLocationByCode(String code) {
        Location location = getLocationCode(code);
        return fullWeatherMapper.mapLocationToFullWeatherDTO(location);
    }

    @Override
    public FullWeatherDTO updateFullWeatherByCode(String code, FullWeatherDTO fullWeatherDTO) {
        Location location = getLocationCode(code);
        Location locationRequest = fullWeatherMapper.mapFullWeatherDTOToLocation(fullWeatherDTO);

        setLocationForWetherData(location, locationRequest);

        saveRealtimeWeatherIfNotExistBefore(locationRequest, location);

        locationRequest.coppyAllFielsFrom(location);
        locationRepository.save(locationRequest);
        return fullWeatherMapper.mapLocationToFullWeatherDTO(locationRequest);
    }

    private void saveRealtimeWeatherIfNotExistBefore(Location locationRequest, Location location) {
        if (Objects.isNull(location.getRealtimeWeather())) {
            location.setRealtimeWeather(locationRequest.getRealtimeWeather());
            locationRepository.save(location);
        }
    }

    private void setLocationForWetherData(Location location, Location locationRequest) {
        RealtimeWeather realtimeWeather = locationRequest.getRealtimeWeather();
        if (Objects.nonNull(realtimeWeather)) {
            realtimeWeather.setLocation(location);
            realtimeWeather.setLastUpdated(LocalDateTime.now());
        }

        List<HourlyWeather> hourlyWeatherList = locationRequest.getHourlyWeatherList();
        hourlyWeatherList.forEach(hourlyWeather -> hourlyWeather.getWeatherId().setLocation(location));

        List<DailyWeather> dailyWeatherList = locationRequest.getDailyWeatherList();
        dailyWeatherList.forEach(dailyWeather -> dailyWeather.getDailyWeatherId().setLocation(location));
    }
}
