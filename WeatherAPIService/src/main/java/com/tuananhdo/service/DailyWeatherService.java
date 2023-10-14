package com.tuananhdo.service;

import com.tuananhdo.entity.DailyWeather;
import payload.LocationDTO;

import java.util.List;

public interface DailyWeatherService {
    List<DailyWeather> getByLocation(LocationDTO locationDTO);
    List<DailyWeather> getByLocationCode(String code);
    List<DailyWeather> updateByLocationCode(String code, List<DailyWeather> dailyWeathers);
}
