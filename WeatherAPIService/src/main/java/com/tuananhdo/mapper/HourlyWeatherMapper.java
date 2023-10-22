package com.tuananhdo.mapper;

import com.tuananhdo.entity.HourlyWeather;
import com.tuananhdo.entity.Location;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import payload.HourlyWeatherDTO;
import payload.HourlyWeatherListDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class HourlyWeatherMapper {

    private final ModelMapper mapper;

    public List<HourlyWeather> mapToHourlyWeatherList(List<HourlyWeatherDTO> hourlyWeatherDTOS) {
        return hourlyWeatherDTOS.stream()
                .map(hourlyWeatherDTO -> mapper.map(hourlyWeatherDTO, HourlyWeather.class))
                .collect(Collectors.toList());
    }

    public HourlyWeatherListDTO mapHourlyWeatherListDTO(List<HourlyWeather> hourlyWeathers) {
        Location location = hourlyWeathers.get(0).getWeatherId().getLocation();
        List<HourlyWeatherDTO> weatherDTOs = hourlyWeathers.stream()
                .map(hourlyWeather -> mapper.map(hourlyWeather, HourlyWeatherDTO.class))
                .collect(Collectors.toList());
        HourlyWeatherListDTO hourlyWeatherListDTO = new HourlyWeatherListDTO();
        hourlyWeatherListDTO.setLocation(location.toString());
        hourlyWeatherListDTO.setHourlyWeatherDTOS(weatherDTOs);
        return hourlyWeatherListDTO;
    }

}
