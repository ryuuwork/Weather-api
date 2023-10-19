package com.tuananhdo.service;

import com.tuananhdo.entity.HourlyWeather;
import payload.LocationDTO;

import java.util.List;

public interface HourlyWeatherService {

    List<HourlyWeather> findByLocationAndHour(LocationDTO locationDTO, int currentHour);

    List<HourlyWeather> getByLocationCode(String code, int currentHour);
    List<HourlyWeather> getByLocation(LocationDTO locationDTO, int currentHour);
    List<HourlyWeather> updateByLocationCode(String code, List<HourlyWeather> hourlyWeathers);
}
