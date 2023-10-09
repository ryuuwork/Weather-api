package com.tuananhdo.repository;

import com.tuananhdo.entity.DailyWeather;
import com.tuananhdo.entity.HourlyWeather;
import com.tuananhdo.entity.Location;
import com.tuananhdo.entity.RealtimeWeather;
import com.tuananhdo.exception.LocationNotFoundException;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import payload.LocationDTO;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LocationRepositoryTests {
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    ModelMapper mapper;

    @Test
    @Order(1)
    public void testAddLocationSuccess() {
        List<LocationDTO> locationDTOs = List.of(
                createLocation("VN_DBP", "VN", "Viet Nam", "Dien Bien", "Dien Bien Phu"),
                createLocation("NYW", "US", "United States of America", "New York", "New York City"),
                createLocation("LON", "UK", "United Kingdom", "London", "London City"),
                createLocation("PAR", "FR", "France", "Paris", "Paris City"),
                createLocation("BER", "DE", "Germany", "Berlin", "Berlin City"),
                createLocation("TOK", "JP", "Japan", "Tokyo", "Tokyo City"),
                createLocation("ROM", "IT", "Italy", "Rome", "Rome City"),
                createLocation("CAN", "CA", "Canada", "Toronto", "Toronto City"),
                createLocation("MAD", "ES", "Spain", "Madrid", "Madrid City"),
                createLocation("SYD", "AU", "Australia", "Sydney", "Sydney City"),
                createLocation("SIN", "SG", "Singapore", "Singapore", "Singapore City")
        );

        List<Location> savedLocation = locationDTOs.stream()
                .map(locationDTO -> mapper.map(locationDTO, Location.class))
                .map(locationRepository::save)
                .collect(Collectors.toList());

        assertThat(savedLocation)
                .isNotNull()
                .hasSize(locationDTOs.size())
                .extracting(Location::getCode, Location::getCityName, Location::getCountryName, Location::getCountryCode)
                .containsExactlyElementsOf(locationDTOs.stream()
                        .map(locationDTO -> Tuple.tuple(locationDTO.getCode(), locationDTO.getCityName(), locationDTO.getCountryName(), locationDTO.getCountryCode()))
                        .collect(Collectors.toList()));

    }

    private LocationDTO createLocation(String code,
                                       String countryCode,
                                       String countryName,
                                       String regionName,
                                       String cityName) {
        LocationDTO location = new LocationDTO();
        location.setCode(code);
        location.setCountryCode(countryCode);
        location.setCountryName(countryName);
        location.setRegionName(regionName);
        location.setCityName(cityName);
        location.setEnabled(true);
        return location;
    }

    @Test
    @Order(2)
    public void testAddRealtimeWeatherData() {
        List<String> locationCodes = List.of("VN_DBP", "NYW", "LON", "PAR", "BER", "TOK", "ROM", "CAN", "MAD", "SYD", "SIN");
        Random random = new Random();
        locationCodes.forEach(code -> {
            Location location = locationRepository.findByCode(code)
                    .orElseThrow(() -> new LocationNotFoundException("Not Found: " + code));

            RealtimeWeather realtimeWeather = location.getRealtimeWeather();
            if (realtimeWeather == null) {
                realtimeWeather = new RealtimeWeather();
                realtimeWeather.setLocation(location);
                location.setRealtimeWeather(realtimeWeather);
            }

            realtimeWeather.setTemperature(random.nextInt(40) - 10);
            realtimeWeather.setHumidity(random.nextInt(101));
            realtimeWeather.setPrecipitation(random.nextInt(101));
            realtimeWeather.setWindSpeed(random.nextInt(50));
            realtimeWeather.setStatus(getRandomWeatherStatus());
            realtimeWeather.setLastUpdated(LocalDateTime.now());

            Location savedLocation = locationRepository.save(location);
            assertThat(savedLocation.getRealtimeWeather().getLocationCode()).isEqualTo(code);
        });
    }

    private String getRandomWeatherStatus() {
        String[] weatherstatuses = {"Sunny", "Cloudy", "Rainy", "Snowy"};
        Random random = new Random();
        return Arrays.stream(weatherstatuses)
                .skip(random.nextInt(weatherstatuses.length))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Weather status Not Found"));
    }

    @Test
    @Order(3)
    public void testListSuccess() {
        List<Location> locations = locationRepository.findUntrashed();
        List<LocationDTO> locationDTO = locations.stream()
                .map(location -> mapper.map(location, LocationDTO.class))
                .toList();
        assertThat(locationDTO).isNotNull();
        locationDTO.forEach(System.out::println);
    }

    @Test
    @Order(4)
    public void testAddHourlyWeatherData() {
        Location location = locationRepository.findByCode("VN_DBP")
                .orElseThrow(() -> new LocationNotFoundException("Not Found Code"));

        List<HourlyWeather> hourlyWeatherList = location.getHourlyWeatherList();

        HourlyWeather hourlyWeather = new HourlyWeather()
                .weatherId(location, 9)
                .temperature(20)
                .precipitation(40)
                .status("Rain");

        HourlyWeather hourlyWeather2 = new HourlyWeather()
                .location(location)
                .hourOfDay(12)
                .temperature(30)
                .precipitation(20)
                .status("Cloudy");

        hourlyWeatherList.add(hourlyWeather);
        hourlyWeatherList.add(hourlyWeather2);

        Location updatedLocation = locationRepository.save(location);

        assertThat(updatedLocation.getHourlyWeatherList()).isNotEmpty();

    }

    @Test
    @Order(5)
    public void testFindByCountryCodeAndCityNameNotFound() {
        String countryCode = "VN_DBPX";
        String cityName = "Dien Bien Phu";
        assertThatThrownBy(() -> {
            locationRepository.findByCountryCodeAndCityName(countryCode, cityName)
                    .orElseThrow(() -> new LocationNotFoundException(countryCode + " : " + cityName));
        }).isInstanceOf(LocationNotFoundException.class)
                .hasMessageContaining(countryCode + " : " + cityName);
    }

    @Test
    @Order(6)
    public void testFindByCountryCodeAndCityNameFound() {
        String countryCode = "VN";
        String cityName = "Dien Bien Phu";
        Location location = locationRepository.findByCountryCodeAndCityName(countryCode, cityName)
                .orElseThrow(() -> new LocationNotFoundException("Not Found: " + countryCode + " : " + cityName));

        assertThat(location).isNotNull();
        assertThat(location.getCountryCode()).isEqualTo(countryCode);
        assertThat(location.getCityName()).isEqualTo(cityName);
    }
    @Test
    @Order(7)
    public void testAddDailyWeatherData(){
        Location location = locationRepository.findByCode("VN_DBP")
                .orElseThrow(() -> new LocationNotFoundException(""));
        List<DailyWeather> dailyWeatherList = location.getDailyWeatherList();

        List<DailyWeather> forecasts = Stream.of(
                        new DailyWeather()
                                .location(location)
                                .dayOfMonth(3)
                                .month(10)
                                .minTemp(23)
                                .maxTemp(35)
                                .precipitation(20)
                                .status("Cool"),
                        new DailyWeather()
                                .location(location)
                                .dayOfMonth(4)
                                .month(10)
                                .minTemp(27)
                                .maxTemp(31)
                                .precipitation(22)
                                .status("Sunny")).toList();
        dailyWeatherList.addAll(forecasts);
        Location saveLocation = locationRepository.save(location);
        assertThat(saveLocation.getDailyWeatherList()).isNotEmpty();
    }
}
