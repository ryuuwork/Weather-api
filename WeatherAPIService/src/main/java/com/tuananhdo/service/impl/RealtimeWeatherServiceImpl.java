package com.tuananhdo.service.impl;

import com.tuananhdo.entity.Location;
import com.tuananhdo.entity.RealtimeWeather;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.exception.RealTimeWeatherNotFoundException;
import com.tuananhdo.mapper.RealtimeWeatherMapper;
import com.tuananhdo.repository.LocationRepository;
import com.tuananhdo.repository.RealtimeWeatherRepository;
import com.tuananhdo.service.AbstractLocationSerivce;
import com.tuananhdo.service.RealtimeWeatherService;
import org.springframework.stereotype.Service;
import payload.LocationDTO;
import payload.RealtimeWeatherDTO;

import java.util.Objects;

@Service
public class RealtimeWeatherServiceImpl extends AbstractLocationSerivce implements RealtimeWeatherService {
    private final RealtimeWeatherRepository realtimeWeatherRepository;
    private final RealtimeWeatherMapper realtimeWeatherMapper;

    public RealtimeWeatherServiceImpl(LocationRepository locationRepository, RealtimeWeatherRepository realtimeWeatherRepository, RealtimeWeatherMapper realtimeWeatherMapper) {
        this.locationRepository = locationRepository;
        this.realtimeWeatherRepository = realtimeWeatherRepository;
        this.realtimeWeatherMapper = realtimeWeatherMapper;
    }

    @Override
    public RealtimeWeatherDTO getByLocation(LocationDTO locationDTO) {
        String countryCode = locationDTO.getCountryCode();
        String cityName = locationDTO.getCityName();
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, cityName)
                .orElseThrow(() -> new LocationNotFoundException(countryCode, cityName));
        return realtimeWeatherMapper.mapToRealtimeWeatherDTO(realtimeWeather);
    }

    @Override
    public RealtimeWeatherDTO getByLocationCode(String code) {
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(code)
                .orElseThrow(() -> new RealTimeWeatherNotFoundException(code));
        return realtimeWeatherMapper.mapToRealtimeWeatherDTO(realtimeWeather);
    }

    @Override
    public RealtimeWeatherDTO updateRealtimeWeather(String code, RealtimeWeatherDTO realtimeWeatherDTO) {
        Location location = getLocationCode(code);
        RealtimeWeather realtimeWeather = location.getRealtimeWeather();
        if (Objects.nonNull(realtimeWeather)){
            realtimeWeather.coppyFielsFrom(realtimeWeatherDTO,location);
        }
        RealtimeWeather updatedRealtimeWeather = realtimeWeatherRepository.save(realtimeWeather);
        return realtimeWeatherMapper.mapToRealtimeWeatherDTO(updatedRealtimeWeather);
    }

}
