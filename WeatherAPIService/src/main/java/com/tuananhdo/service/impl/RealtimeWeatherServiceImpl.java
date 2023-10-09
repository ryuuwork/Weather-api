package com.tuananhdo.service.impl;

import com.tuananhdo.entity.Location;
import com.tuananhdo.entity.RealtimeWeather;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.exception.RealTimeWeatherNotFoundException;
import com.tuananhdo.mapper.RealtimeWeatherMapper;
import com.tuananhdo.repository.LocationRepository;
import com.tuananhdo.repository.RealtimeWeatherRepository;
import com.tuananhdo.service.RealtimeWeatherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import payload.LocationDTO;
import payload.RealtimeWeatherDTO;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RealtimeWeatherServiceImpl implements RealtimeWeatherService {

    private final RealtimeWeatherRepository realtimeWeatherRepository;
    private final LocationRepository locationRepository;
    private final RealtimeWeatherMapper mapper;

    @Override
    public RealtimeWeatherDTO getByLocation(LocationDTO locationDTO) {
        String countryCode = locationDTO.getCountryCode();
        String cityName = locationDTO.getCityName();
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, cityName)
                .orElseThrow(() -> new LocationNotFoundException(countryCode, cityName));
        return mapper.mapToRealtimeWeatherDTO(realtimeWeather);
    }

    @Override
    public RealtimeWeatherDTO getByLocationCode(String code) {
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(code)
                .orElseThrow(() -> new RealTimeWeatherNotFoundException(code));
        return mapper.mapToRealtimeWeatherDTO(realtimeWeather);
    }

    @Override
    public RealtimeWeatherDTO updateRealtimeWeather(String locationCode, RealtimeWeatherDTO realtimeWeatherDTO) {
        Location location = locationRepository.findByCode(locationCode)
                .orElseThrow(() -> new LocationNotFoundException(locationCode));
        RealtimeWeather realtimeWeather = getRealtimeWeather(realtimeWeatherDTO, location);
        RealtimeWeather updatedRealtimeWeather = realtimeWeatherRepository.save(realtimeWeather);
        return mapper.mapToRealtimeWeatherDTO(updatedRealtimeWeather);
    }

    private static RealtimeWeather getRealtimeWeather(RealtimeWeatherDTO realtimeWeatherDTO, Location location) {
        RealtimeWeather realtimeWeather = location.getRealtimeWeather();
        realtimeWeather.setLocation(location);
        realtimeWeather.setPrecipitation(realtimeWeatherDTO.getPrecipitation());
        realtimeWeather.setHumidity(realtimeWeatherDTO.getHumidity());
        realtimeWeather.setTemperature(realtimeWeatherDTO.getTemperature());
        realtimeWeather.setWindSpeed(realtimeWeatherDTO.getWindSpeed());
        realtimeWeather.setStatus(realtimeWeatherDTO.getStatus());
        realtimeWeather.setLastUpdated(LocalDateTime.now());
        return realtimeWeather;
    }
}
