package com.tuananhdo.service.impl;

import com.tuananhdo.entity.Location;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.mapper.LocationMapper;
import com.tuananhdo.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payload.LocationDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class LocationServiceImpl extends AbstractLocationSerivce implements LocationService {

    private final LocationMapper locationMapper;
    @Override
    public LocationDTO addLocation(LocationDTO locationDTO) {
        Location location = locationMapper.mapToLocation(locationDTO);
        Location savedLocation = locationRepository.save(location);
        return locationMapper.mapToLocationDTO(savedLocation);
    }

    @Override
    public List<LocationDTO> findUntrashedLocation() {
        List<Location> location = locationRepository.findUntrashed();
        return location.stream()
                .map(locationMapper::mapToLocationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LocationDTO findLocationByCode(String code) throws LocationNotFoundException {
        Location location = getLocationCode(code);
        return locationMapper.mapToLocationDTO(location);
    }

    @Override
    public void deleteLocation(String code) {
        Location location = getLocationCode(code);
        locationRepository.delete(location);
    }

    @Override
    public LocationDTO updateLocation(LocationDTO locationDTO, String code) {
        Location location = getLocationCode(code);
        Location locationRequest = locationMapper.mapToLocation(locationDTO);
        location.coppyFielsFrom(locationRequest);
        Location updatedLocation = locationRepository.save(location);
        return locationMapper.mapToLocationDTO(updatedLocation);
    }
}
