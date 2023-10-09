package com.tuananhdo.service;

import com.tuananhdo.exception.LocationNotFoundException;
import payload.LocationDTO;

import java.util.List;

public interface LocationService {
    LocationDTO addLocation(LocationDTO location);
    List<LocationDTO> findUntrashedLocation();
    LocationDTO findLocationByCode(String code) throws LocationNotFoundException;
    LocationDTO updateLocation(LocationDTO locationDTO,String code);
    void deleteLocation(String code);
}
