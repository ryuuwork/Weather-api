package com.tuananhdo.service;

import com.tuananhdo.entity.HourlyWeather;
import payload.HourlyWeatherDTO;
import payload.LocationDTO;

import java.util.List;

public interface HourlyWeatherService {

    List<HourlyWeather> findByLocationAndHour(LocationDTO location, int currentHour);

    List<HourlyWeatherDTO> getByLocationCode(String locationCode, int currentHour);

    List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> hourlyWeathers);
}
