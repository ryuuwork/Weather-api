package com.tuananhdo.mapper;

import com.tuananhdo.entity.Location;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import payload.FullWeatherDTO;

@Component
@AllArgsConstructor
public class FullWeatherMapper {
    private final ModelMapper mapper;

    public FullWeatherDTO mapLocationToFullWeatherDTO(Location location) {
        FullWeatherDTO fullWeatherDTO = mapper.map(location, FullWeatherDTO.class);
        fullWeatherDTO.getRealtimeWeather().setLocation(null);
        return fullWeatherDTO;
    }

    public Location mapFullWeatherDTOToLocation(FullWeatherDTO fullWeatherDTO) {
        return mapper.map(fullWeatherDTO, Location.class);
    }
}
