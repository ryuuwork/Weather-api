package com.tuananhdo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty
    private String regionName;
    @Column(length = 64, nullable = false, unique = true)
    private String countryName;
    @Column(length = 64, nullable = false, unique = true)
    private String countryCode;
    private boolean enabled;
    @JsonIgnore
    private boolean trashed;
    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private RealtimeWeather realtimeWeather;

    @OneToMany(mappedBy = "weatherId.location", cascade = CascadeType.ALL)
    private List<HourlyWeather> hourlyWeatherList = new ArrayList<>();

    @OneToMany(mappedBy = "dailyWeatherId.location", cascade = CascadeType.ALL)
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

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
