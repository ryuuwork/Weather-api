package com.tuananhdo.service.impl;

import com.tuananhdo.entity.Location;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.mapper.LocationMapper;
import com.tuananhdo.repository.LocationRepository;
import com.tuananhdo.service.AbstractLocationSerivce;
import com.tuananhdo.service.LocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payload.LocationDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class LocationServiceImpl extends AbstractLocationSerivce implements LocationService {
    private final LocationMapper locationMapper;

    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    public LocationDTO addLocation(LocationDTO locationDTO) {
        Location location = locationMapper.mapToLocation(locationDTO);
        Location savedLocation = locationRepository.save(location);
        return locationMapper.mapToLocationDTO(savedLocation);
    }

    @Override
    public List<LocationDTO> findUntrashedLocation() {
        List<Location> location = locationRepository.findTrashed();
        return location.stream()
                .map(locationMapper::mapToLocationDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Deprecated
    public Page<LocationDTO> listByPage(int pageNumber, int pageSize, String sortField) {
        Sort sort = Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Location> location = locationRepository.findTrashed(pageable);
        return location.map(locationMapper::mapToLocationDTO);
    }

    @Override
    public Page<LocationDTO> listByPage(int pageNumber, int pageSize,
                                        String sortField,
                                        Map<String, Object> filterFields) {
        Sort sort = Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Location> location = locationRepository.listWithFilter(pageable,filterFields);
        return location.map(locationMapper::mapToLocationDTO);
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
