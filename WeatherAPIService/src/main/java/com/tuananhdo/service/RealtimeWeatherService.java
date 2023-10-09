package com.tuananhdo.service;

import payload.LocationDTO;
import payload.RealtimeWeatherDTO;

public interface RealtimeWeatherService {
    RealtimeWeatherDTO getByLocation(LocationDTO locationDTO);

    RealtimeWeatherDTO getByLocationCode(String code);

    RealtimeWeatherDTO updateRealtimeWeather(String locationCode, RealtimeWeatherDTO realtimeWeatherDTO);
}
