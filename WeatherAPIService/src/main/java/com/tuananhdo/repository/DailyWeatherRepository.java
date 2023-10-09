package com.tuananhdo.repository;

import com.tuananhdo.entity.DailyWeather;
import com.tuananhdo.entity.DailyWeatherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyWeatherRepository extends JpaRepository<DailyWeather, DailyWeatherId> {

    @Query("""
            SELECT d FROM DailyWeather d WHERE d.dailyWeatherId.location.code = :code
            AND d.dailyWeatherId.location.trashed = false
            """)
    List<DailyWeather> findByLocationCode(String code);
}
