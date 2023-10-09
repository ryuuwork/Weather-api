package com.tuananhdo.repository;

import com.tuananhdo.entity.RealtimeWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RealtimeWeatherRepository extends JpaRepository<RealtimeWeather, String> {

    @Query("SELECT r FROM RealtimeWeather r " +
            "WHERE r.location.countryCode= :countryCode " +
            "AND r.location.cityName = :cityName")
    Optional<RealtimeWeather> findByCountryCodeAndCity(String countryCode, String cityName);

    @Query("SELECT r from RealtimeWeather r WHERE r.location.code = :locationCode AND r.location.trashed = false ")
    Optional<RealtimeWeather> findByLocationCode(String locationCode);
}
