package com.tuananhdo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @Column(length = 12, nullable = false, unique = true)
    private String code;
    @Column(length = 128, nullable = false)
    private String cityName;
    @Column(length = 128, nullable = false, unique = true)
    private String regionName;
    @Column(length = 64, nullable = false, unique = true)
    private String countryName;
    @Column(length = 64, nullable = false, unique = true)
    private String countryCode;
    private boolean enabled;
    private boolean trashed;
    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private RealtimeWeather realtimeWeather;

    @OneToMany(mappedBy = "weatherId.location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HourlyWeather> hourlyWeatherList = new ArrayList<>();

    @OneToMany(mappedBy = "dailyWeatherId.location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyWeather> dailyWeatherList = new ArrayList<>();

    public Location(String cityName, String regionName, String countryName, String countryCode) {
        this.cityName = cityName;
        this.regionName = regionName;
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    public Location code(String code) {
        setCode(code);
        return this;
    }

    @Override
    public String toString() {
        return cityName + "," + (regionName != null ? regionName : "") + "," + countryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(code, location.code);
    }

    public void coppyFielsFrom(Location anotherLocation) {
        setCountryCode(anotherLocation.getCountryCode());
        setCountryName(anotherLocation.getCountryName());
        setCityName(anotherLocation.getCityName());
        setRegionName(anotherLocation.getRegionName());
        setEnabled(anotherLocation.isEnabled());
    }

    public void coppyAllFielsFrom(Location anotherLocation) {
        coppyFielsFrom(anotherLocation);
        setCode(anotherLocation.getCode());
        setTrashed(anotherLocation.isTrashed());
    }

    public void setCodee(String code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
