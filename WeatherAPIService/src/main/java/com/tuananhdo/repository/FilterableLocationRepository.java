package com.tuananhdo.repository;

import com.tuananhdo.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import payload.LocationDTO;

import java.util.Map;
import java.util.Objects;

public interface FilterableLocationRepository {
    Page<Location> listWithFilter(Pageable pageable, Map<String, Object> filterFields);
}
