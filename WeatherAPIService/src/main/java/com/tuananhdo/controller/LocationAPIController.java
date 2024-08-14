package com.tuananhdo.controller;

import com.tuananhdo.exception.BadRequestException;
import com.tuananhdo.repository.FilterableLocationRepository;
import com.tuananhdo.service.LocationService;
import com.tuananhdo.utils.SortField;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import payload.LocationDTO;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/locations")
@Validated
public class LocationAPIController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationDTO> addLocation(@RequestBody @Valid LocationDTO locationDTO) throws IOException {
        LocationDTO location = locationService.addLocation(locationDTO);
        URI uri = URI.create("/api/v1/locations/" + locationDTO.getCode());
        return ResponseEntity.created(uri).body(addLinks(location));
    }


    @Deprecated
    public ResponseEntity<List<LocationDTO>> listAllUntrashedLocations() {
        List<LocationDTO> location = locationService.findUntrashedLocation();
        if (location.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(location);
    }

    @GetMapping
    public ResponseEntity<?> listLocations(@RequestParam(value = "page", required = false, defaultValue = "1")
                                           @Min(value = 1) Integer pageNumber,
                                           @RequestParam(value = "size", required = false, defaultValue = "5")
                                           @Min(value = 5) @Max(20) Integer pageSize,
                                           @RequestParam(value = "sort", required = false, defaultValue = "code") String sortField,
                                           @RequestParam(value = "enabled", required = false, defaultValue = "") String enabled,
                                           @RequestParam(value = "region_name", required = false, defaultValue = "") String regionName,
                                           @RequestParam(value = "country_code", required = false, defaultValue = "") String countryCode) throws BadRequestException {
        if (Arrays.stream(SortField.values()).
                noneMatch(e -> e.getFieldName().equals(sortField))) {
            throw new BadRequestException("No valid code found: " + sortField);
        }
        Map<String, Object> filterFields = new HashMap<>();

        Page<LocationDTO> locationPage = locationService.listByPage(pageNumber - 1, pageSize, sortField, filterFields);

        List<LocationDTO> locationList = locationPage.getContent();
        if (locationList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(addPageMetadataAndCollectionLinks(locationList, locationPage, sortField, enabled, regionName, countryCode));
    }

    private CollectionModel<LocationDTO> addPageMetadataAndCollectionLinks(List<LocationDTO> locationDTOS,
                                                                           Page<LocationDTO> pageInfo,
                                                                           String sortField,
                                                                           String enabled,
                                                                           String regionName,
                                                                           String countryCode) throws BadRequestException {

        String actualEnabled = "".equals(enabled) ? null : enabled;
        String actualRegionName = "".equals(regionName) ? null : regionName;
        String actualCountryCode = "".equals(countryCode) ? null : countryCode;

        locationDTOS.forEach(locationDTO ->
        {
            try {
                locationDTO.add(linkTo(methodOn(LocationAPIController.class)
                        .getLocationByCode(locationDTO.getCode()))
                        .withSelfRel());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        int pageNumber = pageInfo.getNumber() + 1;
        int pageSize = pageInfo.getSize();
        long totalElements = pageInfo.getTotalElements();
        int totalPages = pageInfo.getTotalPages();


        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pageSize, pageNumber, totalElements);
        CollectionModel<LocationDTO> collectionModel = PagedModel.of(locationDTOS, pageMetadata);
        collectionModel.add(linkTo(methodOn(LocationAPIController.class)
                .listLocations(pageNumber, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
                .withSelfRel());

        if (pageNumber > 1) {
            collectionModel.add(linkTo(methodOn(LocationAPIController.class)
                    .listLocations(1, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
                    .withRel(IanaLinkRelations.FIRST));


            collectionModel.add(linkTo(methodOn(LocationAPIController.class)
                    .listLocations(pageNumber - 1, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
                    .withRel(IanaLinkRelations.PREVIOUS));
        }
        if (pageNumber < totalPages) {
            collectionModel.add(linkTo(methodOn(LocationAPIController.class)
                    .listLocations(pageNumber + 1, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
                    .withRel(IanaLinkRelations.NEXT));
            collectionModel.add(linkTo(methodOn(LocationAPIController.class)
                    .listLocations(totalPages, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
                    .withRel(IanaLinkRelations.LAST));
        }
        return collectionModel;
    }

    @GetMapping("/{code}")
    public ResponseEntity<LocationDTO> getLocationByCode(@PathVariable("code") String code) throws IOException {
        LocationDTO location = locationService.findLocationByCode(code);
        if (Objects.isNull(location)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(addLinks(location));
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

    private LocationDTO addLinks(LocationDTO dto) throws IOException {
        dto.add(linkTo(methodOn(LocationAPIController.class).getLocationByCode(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(RealtimeWeatherAPIController.class).getRealtimeWeatherByLocationCode(dto.getCode())).withRel("realtime_weather"));
        dto.add(linkTo(methodOn(HourlyWeatherAPIController.class).getHourlyForecastByLocationCode(dto.getCode(),null)).withRel("hourly_weather"));
        dto.add(linkTo(methodOn(DailyWeatherAPIController.class).getDailyForecastByLocationCode(dto.getCode())).withRel("daily_weather"));
        dto.add(linkTo(methodOn(FullWeatherAPIController.class).getFullWeatherByLocationCode(dto.getCode())).withRel("full_weather"));
        return dto;
    }
}
