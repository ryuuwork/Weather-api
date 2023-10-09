package com.tuananhdo.repository;

import com.tuananhdo.entity.RealtimeWeather;
import com.tuananhdo.exception.RealTimeWeatherNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RealtimeWeatherRepositoryTests {

    @Autowired
    private RealtimeWeatherRepository realtimeWeatherRepository;

    @Test
    public void testUpdateRealtimeWeather() {
        String locationCode = "VN_DBP";
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findById(locationCode)
                .orElseThrow(() -> new RealTimeWeatherNotFoundException("Not Found: " + locationCode));

        realtimeWeather.setTemperature(9);
        realtimeWeather.setHumidity(15);
        realtimeWeather.setPrecipitation(23);
        realtimeWeather.setWindSpeed(11);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setLastUpdated(LocalDateTime.now());
        RealtimeWeather updatedRealtimeWeather = realtimeWeatherRepository.save(realtimeWeather);
        assertThat(updatedRealtimeWeather.getWindSpeed()).isEqualTo(11);
        assertThat(updatedRealtimeWeather.getTemperature()).isEqualTo(9);
    }

    @Test
    public void testFindByCountryCodeAndCityNameNotFound() {
        String countryCode = "DEX=";
        String cityName = "Berlin CityX";

        Optional<RealtimeWeather> realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, cityName);
        assertThat(realtimeWeather).isEmpty();

    }

    @Test
    public void testFindByCountryCodeAndCityNameFound() {
        String countryCode = "DE";
        String cityName = "Berlin City";

        Optional<RealtimeWeather> realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, cityName);
        assertThat(realtimeWeather).isPresent();
        assertThat(realtimeWeather.get().getLocation().getCityName()).isEqualTo(cityName);
        assertThat(realtimeWeather.get().getLocation().getCountryCode()).isEqualTo(countryCode);

    }
}
