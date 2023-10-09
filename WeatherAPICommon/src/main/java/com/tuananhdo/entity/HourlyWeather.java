package com.tuananhdo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hourly_weather")
public class HourlyWeather {
    @EmbeddedId
    private HourlyWeatherId weatherId = new HourlyWeatherId();
    private int temperature;
    private int precipitation;
    @Column(length = 50)
    private String status;

    public HourlyWeather temperature(int temperature) {
        setTemperature(temperature);
        return this;
    }

    public HourlyWeather weatherId(Location location, int hour) {
        this.weatherId.setHourOfDay(hour);
        this.weatherId.setLocation(location);
        return this;
    }

    public HourlyWeather precipitation(int precipitation) {
        setPrecipitation(precipitation);
        return this;
    }

    public HourlyWeather status(String status) {
        setStatus(status);
        return this;
    }

    public HourlyWeather location(Location location) {
        this.weatherId.setLocation(location);
        return this;
    }

    public HourlyWeather hourOfDay(int hour) {
        this.weatherId.setHourOfDay(hour);
        return this;
    }

    @Override
    public String toString() {
        return "HourlyWeather{" +
                "weatherId=" + weatherId +
                ", temperature=" + temperature +
                ", precipitation=" + precipitation +
                ", status='" + status + '\'' +
                '}';
    }
}
