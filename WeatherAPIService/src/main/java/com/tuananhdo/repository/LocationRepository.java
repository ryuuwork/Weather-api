package com.tuananhdo.repository;

import com.tuananhdo.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l WHERE l.trashed = false ")
    List<Location> findUntrashed();

    @Query("SELECT l FROM Location l WHERE l.trashed = false AND l.code =:code")
    Optional<Location> findByCode(String code);

    @Query("SELECT l FROM Location l WHERE l.countryCode =:countryCode AND l.cityName =:cityName aND l.trashed=false ")
    Optional<Location> findByCountryCodeAndCityName(String countryCode, String cityName);

}
