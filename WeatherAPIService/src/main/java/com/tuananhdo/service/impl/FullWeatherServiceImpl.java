package com.tuananhdo.service.impl;

import com.tuananhdo.entity.DailyWeather;
import com.tuananhdo.entity.HourlyWeather;
import com.tuananhdo.entity.Location;
import com.tuananhdo.entity.RealtimeWeather;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.mapper.FullWeatherMapper;
import com.tuananhdo.repository.LocationRepository;
import com.tuananhdo.service.FullWeatherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import payload.FullWeatherDTO;
import payload.LocationDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class FullWeatherServiceImpl implements FullWeatherService {

    private final LocationRepository locationRepository;
    private final FullWeatherMapper fullWeatherMapper;

    @Override
    public FullWeatherDTO getByLocation(LocationDTO locationDTO) {
        String countryCode = locationDTO.getCountryCode();
        String cityName = locationDTO.getCityName();
        Location location = locationRepository.findByCountryCodeAndCityName(countryCode, cityName)
                .orElseThrow(() -> new LocationNotFoundException(countryCode, cityName));
        return fullWeatherMapper.mapLocationToFullWeatherDTO(location);
    }

    @Override
    public FullWeatherDTO getLocationByCode(String code) {
        Location location = locationRepository.findByCode(code)
                .orElseThrow(() -> new LocationNotFoundException(code));
        return fullWeatherMapper.mapLocationToFullWeatherDTO(location);
    }

    @Override
    public FullWeatherDTO updateFullWeatherByCode(String code, FullWeatherDTO fullWeatherDTO) {
        Location location = locationRepository.findByCode(code)
                .orElseThrow(() -> new LocationNotFoundException(code));
        Location locationRequest = fullWeatherMapper.mapFullWeatherDTOToLocation(fullWeatherDTO);

        RealtimeWeather realtimeWeather = locationRequest.getRealtimeWeather();
        realtimeWeather.setLocation(location);
        realtimeWeather.setLastUpdated(LocalDateTime.now());
        if (Objects.isNull(location.getRealtimeWeather())) {
            location.setRealtimeWeather(realtimeWeather);
            locationRepository.save(location);
        }

        List<DailyWeather> dailyWeatherList = locationRequest.getDailyWeatherList();
        dailyWeatherList.forEach(dailyWeather -> dailyWeather.getDailyWeatherId().setLocation(location));

        List<HourlyWeather> hourlyWeatherList = locationRequest.getHourlyWeatherList();
        hourlyWeatherList.forEach(hourlyWeather -> hourlyWeather.getWeatherId().setLocation(location));

        locationRequest.setCode(location.getCode());
        locationRequest.setCityName(location.getCityName());
        locationRequest.setRegionName(location.getRegionName());
        locationRequest.setCountryCode(location.getCountryCode());
        locationRequest.setCountryName(location.getCountryName());
        locationRequest.setEnabled(location.isEnabled());
        locationRequest.setTrashed(location.isTrashed());
        locationRepository.save(locationRequest);
        return fullWeatherMapper.mapLocationToFullWeatherDTO(locationRequest);
    }
}
