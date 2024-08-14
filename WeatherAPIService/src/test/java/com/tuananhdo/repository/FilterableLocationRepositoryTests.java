package com.tuananhdo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.tuananhdo.entity.Location;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FilterableLocationRepositoryTests {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testListWithDefaults() {
        int pageSize = 5;
        int pageNumber = 0;
        String sortField = "code";

        Sort sort = Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Location> page = locationRepository.listWithFilter(pageable, Collections.emptyMap());

        List<Location> content = page.getContent();

        System.out.println("Total element: " + page.getTotalElements());

        assertThat(page.getTotalElements()).isGreaterThan(pageable.getOffset() + content.size());

        assertThat(content).size().isEqualTo(pageSize);
        assertThat(content).isSortedAccordingTo(new Comparator<Location>() {
            @Override
            public int compare(Location l1, Location l2) {
                return l1.getCode().compareTo(l2.getCode());
            }
        });

        content.forEach(System.out::println);
    }

    @Test
    public void testListNoFilterSortedByCityName() {
        int pageSize = 5;
        int pageNumber = 0;
        String sortField = "cityName";
        Sort sort = Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Location> page = locationRepository.listWithFilter(pageable, Collections.emptyMap());

        List<Location> content = page.getContent();
        assertThat(content).size().isEqualTo(pageSize);
        assertThat(content).isSortedAccordingTo(new Comparator<Location>() {
            @Override
            public int compare(Location l1, Location l2) {
                return l1.getCityName().compareTo(l2.getCityName());
            }
        });

        content.forEach(System.out::println);
    }

    @Test
    public void testListFilteredRegionNameSortedByCityName() {
        int pageSize = 1;
        int pageNumber = 0;
        String sortField = "cityName";
        String regionName = "Berlin";
        Sort sort = Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Map<String, Object> filterFields = new HashMap<>();
        filterFields.put("regionName", regionName);
        Page<Location> page = locationRepository.listWithFilter(pageable, filterFields);

        List<Location> content = page.getContent();
        assertThat(content).size().isEqualTo(pageSize);
        assertThat(content).isSortedAccordingTo(new Comparator<Location>() {
            @Override
            public int compare(Location l1, Location l2) {
                return l1.getCityName().compareTo(l2.getCityName());
            }
        });

        content.forEach(location -> assertThat(location.getRegionName()).isEqualTo(regionName));

        content.forEach(System.out::println);
    }

    @Test
    public void testListFilteredCountryCodeSortedByCode() {
        int pageSize = 1;
        int pageNumber = 0;
        String sortField = "cityName";
        String countryCode = "DE";
        Sort sort = Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Map<String, Object> filterFields = new HashMap<>();
        filterFields.put("countryCode", countryCode);
        Page<Location> page = locationRepository.listWithFilter(pageable, filterFields);

        List<Location> content = page.getContent();
        assertThat(content).size().isEqualTo(pageSize);
        assertThat(content).isSortedAccordingTo(new Comparator<Location>() {
            @Override
            public int compare(Location l1, Location l2) {
                return l1.getCode().compareTo(l2.getCode());
            }
        });

        content.forEach(location -> assertThat(location.getCountryCode()).isEqualTo(countryCode));

        content.forEach(System.out::println);
    }

    @Test
    public void testListFilteredCountryCodeAndEnabledSortedByCityName() {
        int pageSize = 1;
        int pageNumber = 0;

        String sortField = "cityName";
        String countryCode = "DE";
        boolean enabled = true;

        Sort sort = Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Map<String, Object> filterFields = new HashMap<>();
        filterFields.put("countryCode", countryCode);
        filterFields.put("enabled", enabled);

        Page<Location> page = locationRepository.listWithFilter(pageable, filterFields);

        List<Location> content = page.getContent();

        assertThat(content).size().isEqualTo(pageSize);
        assertThat(content).isSortedAccordingTo(new Comparator<Location>() {
            @Override
            public int compare(Location l1, Location l2) {
                return l1.getCode().compareTo(l2.getCode());
            }
        });

        content.forEach(location -> assertThat(location.getCountryCode()).isEqualTo(countryCode));
        content.forEach(location -> assertThat(location.isEnabled()).isTrue());

        content.forEach(System.out::println);
    }

}
