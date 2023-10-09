package com.tuananhdo.mapper;

import com.tuananhdo.entity.Location;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import payload.LocationDTO;

@Component
@AllArgsConstructor
public class LocationMapper {
    private final ModelMapper mapper;

    public Location mapToLocation(LocationDTO locationDTO) {
        return mapper.map(locationDTO, Location.class);
    }

    public LocationDTO mapToLocationDTO(Location location) {
        return mapper.map(location, LocationDTO.class);
    }
}
