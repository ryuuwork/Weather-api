package com.tuananhdo.repository;

import com.tuananhdo.entity.DailyWeather;
import com.tuananhdo.entity.DailyWeatherId;
import com.tuananhdo.entity.Location;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class DailyForeCastWeatherRepositoryTests {

    @Autowired
    private DailyWeatherRepository dailyWeatherRepository;

    @Test
    public void testAddDailyWeather() {
        String locationCode = "VN_DBP";
        Location location = new Location()
                .code(locationCode);
        List<DailyWeather> forecast = Stream.of(
                new DailyWeather()
                        .location(location)
                        .dayOfMonth(7)
                        .month(10)
                        .minTemp(27)
                        .maxTemp(31)
                        .precipitation(22)
                        .status("Clear"),
                new DailyWeather()
                        .location(location)
                        .dayOfMonth(8)
                        .month(10)
                        .minTemp(22)
                        .maxTemp(37)
                        .precipitation(22)
                        .status("Cloudy")).toList();
        List<DailyWeather> saveDailyWeatherList = dailyWeatherRepository.saveAll(forecast);
        saveDailyWeatherList.forEach(saveDailyWeather -> {
            assertThat(saveDailyWeather.getDailyWeatherId().getLocation().getCode()).isEqualTo(locationCode);
        });
    }

    @Test
    public void testDeleteDailyWeather() {
        String locationCode = "THAI";
        Location location = new Location()
                .code(locationCode);
        DailyWeatherId dailyWeatherId = new DailyWeatherId(7, 10, location);
        dailyWeatherRepository.deleteById(dailyWeatherId);
        Optional<DailyWeather> resultDelete = dailyWeatherRepository.findById(dailyWeatherId);
        assertThat(resultDelete).isNotPresent();
    }
}
