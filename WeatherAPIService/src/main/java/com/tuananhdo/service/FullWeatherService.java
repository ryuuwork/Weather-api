package com.tuananhdo.service;

import payload.FullWeatherDTO;
import payload.LocationDTO;

public interface FullWeatherService {
    FullWeatherDTO getByLocation(LocationDTO locationDTO);
    FullWeatherDTO getLocationByCode(String code);
    FullWeatherDTO updateFullWeatherByCode(String code, FullWeatherDTO fullWeatherDTO);
}
