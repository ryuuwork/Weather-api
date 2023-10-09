package com.tuananhdo.controller;

import com.tuananhdo.service.LocationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payload.LocationDTO;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/locations")
public class LocationAPIController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationDTO> addLocation(@RequestBody @Valid LocationDTO locationDTO) {
        LocationDTO addedLocation = locationService.addLocation(locationDTO);
        URI uri = URI.create("/api/v1/locations/" + locationDTO.getCode());
        return ResponseEntity.created(uri).body(addedLocation);
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> listAllUntrashedLocations() {
        List<LocationDTO> location = locationService.findUntrashedLocation();
        if (location.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(location);
    }

    @GetMapping("/{code}")
    public ResponseEntity<LocationDTO> getLocation(@PathVariable("code") String code) {
        LocationDTO location = locationService.findLocationByCode(code);
        if (Objects.isNull(location)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(location);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateLocation(@RequestBody @Valid LocationDTO locationDTO,
                                            @PathVariable("code") String code) {
            LocationDTO location = locationService.updateLocation(locationDTO, code);
            if (Objects.isNull(location)) {
                return ResponseEntity.noContent().build();
            }
            return new ResponseEntity<>("Location updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteLocationByCode(@PathVariable("code") String code) {
        locationService.deleteLocation(code);
        return new ResponseEntity<>("Location deleted successfully", HttpStatus.OK);
    }
}
