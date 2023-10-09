package com.tuananhdo.repository;

import com.tuananhdo.entity.HourlyWeather;
import com.tuananhdo.entity.HourlyWeatherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HourlyWeatherRepository extends JpaRepository<HourlyWeather, HourlyWeatherId> {

    @Query("""
            select h from HourlyWeather h
            where h.weatherId.location.code = :locationCode\s
            and h.weatherId.hourOfDay >:currentHour\s
            and h.weatherId.location.trashed = false
            """)
    List<HourlyWeather> findByLocationCodeAndHour(String locationCode, int currentHour);
}
