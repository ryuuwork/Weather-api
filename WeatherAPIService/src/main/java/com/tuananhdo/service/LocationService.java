package com.tuananhdo.service;

import com.tuananhdo.exception.LocationNotFoundException;
import org.springframework.data.domain.Page;
import payload.LocationDTO;

import java.util.List;
import java.util.Map;

public interface LocationService {
    LocationDTO addLocation(LocationDTO location);
    List<LocationDTO> findUntrashedLocation();
    Page<LocationDTO> listByPage(int pageNumber,int pageSize, String sortField);
    Page<LocationDTO> listByPage(int pageNumber, int pageSize, String sortField, Map<String,Object> filterFields);
    LocationDTO findLocationByCode(String code) throws LocationNotFoundException;
    LocationDTO updateLocation(LocationDTO locationDTO,String code);
    void deleteLocation(String code);
}
