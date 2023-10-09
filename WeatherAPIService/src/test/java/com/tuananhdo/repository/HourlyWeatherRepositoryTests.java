package com.tuananhdo.repository;

import com.tuananhdo.entity.HourlyWeather;
import com.tuananhdo.entity.HourlyWeatherId;
import com.tuananhdo.entity.Location;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import payload.LocationDTO;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HourlyWeatherRepositoryTests {

    @Autowired
    HourlyWeatherRepository hourlyWeatherRepository;
    @Autowired
    ModelMapper mapper;

    @Test
    public void testAddHourlyWeather() {
        String locationCode = "VN_DBP";
        int hourOfDay = 16;

        LocationDTO location = new LocationDTO()
                .code(locationCode);

        Location mapLocation = mapper.map(location,Location.class);

        HourlyWeather hourlyWeather = new HourlyWeather()
                .weatherId(mapLocation, hourOfDay)
                .temperature(12)
                .precipitation(13)
                .status("Cloudy");

        HourlyWeather updatedHourlyWeather = hourlyWeatherRepository.save(hourlyWeather);
        assertThat(updatedHourlyWeather.getWeatherId().getLocation().getCode()).isEqualTo(locationCode);
        assertThat(updatedHourlyWeather.getWeatherId().getHourOfDay()).isEqualTo(hourOfDay);
    }

    @Test
    public void testDeleteHourlyWeather(){
        LocationDTO location = new LocationDTO().code("VN_DBP");
        Location mapLocation = mapper.map(location,Location.class);
        HourlyWeatherId weatherId = new HourlyWeatherId();
        weatherId.setLocation(mapLocation);
        weatherId.setHourOfDay(9);
        hourlyWeatherRepository.deleteById(weatherId);
        Optional<HourlyWeather> hourlyWeather = hourlyWeatherRepository.findById(weatherId);
        assertThat(hourlyWeather).isNotPresent();
    }

    @Test
    public void testFindByLocationCodeAndHourFound() {
        String locationCode = "VN_DBP";
        int currentHour = 12;

        List<HourlyWeather> hourlyWeatherList =
                hourlyWeatherRepository.findByLocationCodeAndHour(locationCode,currentHour);
        assertThat(hourlyWeatherList).isNotEmpty();
    }


    @Test
    public void testFindByLocationCodeAndHourNotFound() {
        String locationCode = "VN_DBP";
        int currentHour = 17;

        List<HourlyWeather> hourlyWeatherList =
                hourlyWeatherRepository.findByLocationCodeAndHour(locationCode,currentHour);
        assertThat(hourlyWeatherList).isEmpty();
    }

}
