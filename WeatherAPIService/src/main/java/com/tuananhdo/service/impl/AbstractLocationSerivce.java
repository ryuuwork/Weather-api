package com.tuananhdo.service.impl;

import com.tuananhdo.entity.Location;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import payload.LocationDTO;

public abstract class AbstractLocationSerivce {
    @Autowired protected LocationRepository locationRepository;
    protected Location getLocationCode(String code) {
        return locationRepository.findByCode(code)
                .orElseThrow(() -> new LocationNotFoundException(code));
    }

    protected Location getCountryCodeAndCityName(LocationDTO locationDTO) {
        String countryCode = locationDTO.getCountryCode();
        String cityName = locationDTO.getCityName();
        return locationRepository.findByCountryCodeAndCityName(countryCode, cityName)
                .orElseThrow(() -> new LocationNotFoundException(countryCode, cityName));
    }
}
