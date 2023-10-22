package com.tuananhdo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import payload.RealtimeWeatherDTO;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "realtime_weather")
public class RealtimeWeather {
    @Id
    @Column(name = "location_code")
    private String locationCode;
    private int temperature;
    private int humidity;
    private int precipitation;
    private int windSpeed;
    @Column(length = 50)
    private String status;
    @JsonProperty("last_updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastUpdated;
    @OneToOne
    @JoinColumn(name = "location_code")
    @MapsId
    private Location location;

    public void setLocation(Location location) {
        this.locationCode = location.getCode();
        this.location = location;
    }

    public RealtimeWeather temperature(int temperature) {
        setTemperature(temperature);
        return this;
    }

    public RealtimeWeather humidity(int humidity) {
        setHumidity(humidity);
        return this;
    }

    public RealtimeWeather precipitation(int precipitation) {
        setPrecipitation(precipitation);
        return this;
    }

    public RealtimeWeather windSpeed(int windSpeed) {
        setWindSpeed(windSpeed);
        return this;
    }

    public RealtimeWeather status(String status) {
        setStatus(status);
        return this;
    }

    public RealtimeWeather lastUpdated(LocalDateTime date) {
        setLastUpdated(date);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealtimeWeather that = (RealtimeWeather) o;
        return Objects.equals(locationCode, that.locationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationCode);
    }

    public void coppyFielsFrom(RealtimeWeatherDTO another, Location location){
        setLocation(location);
        setPrecipitation(another.getPrecipitation());
        setHumidity(another.getHumidity());
        setTemperature(another.getTemperature());
        setWindSpeed(another.getWindSpeed());
        setStatus(another.getStatus());
        setLastUpdated(LocalDateTime.now());
    }
}
