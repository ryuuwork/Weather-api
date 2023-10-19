package com.tuananhdo.mapper;

import com.tuananhdo.entity.RealtimeWeather;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import payload.RealtimeWeatherDTO;

@Component
@AllArgsConstructor
public class RealtimeWeatherMapper {
    private final ModelMapper mapper;
    public RealtimeWeatherDTO mapToRealtimeWeatherDTO(RealtimeWeather realtimeWeather) {
        return mapper.map(realtimeWeather, RealtimeWeatherDTO.class);
    }
}
