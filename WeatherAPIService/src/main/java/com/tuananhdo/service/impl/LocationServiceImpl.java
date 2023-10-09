package com.tuananhdo.service.impl;

import com.tuananhdo.entity.Location;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.mapper.LocationMapper;
import com.tuananhdo.repository.LocationRepository;
import com.tuananhdo.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import payload.LocationDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
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
        Location location = locationRepository.findByCode(code)
                .orElseThrow(() -> new LocationNotFoundException(code));
        return locationMapper.mapToLocationDTO(location);
    }

    @Override
    public void deleteLocation(String code) {
        Location location = locationRepository.findByCode(code)
                .orElseThrow(() -> new LocationNotFoundException(code));
        locationRepository.delete(location);
    }

    @Override
    public LocationDTO updateLocation(LocationDTO locationDTO, String code) {
        Location location = locationRepository.findByCode(code)
                .orElseThrow(() -> new LocationNotFoundException(code));
        location.setCityName(locationDTO.getCityName());
        location.setRegionName(locationDTO.getRegionName());
        location.setCountryName(locationDTO.getCountryName());
        location.setCountryCode(locationDTO.getCountryCode());
        location.setEnabled(locationDTO.isEnabled());
        Location updatedLocation = locationRepository.save(location);
        return locationMapper.mapToLocationDTO(updatedLocation);
    }
}
