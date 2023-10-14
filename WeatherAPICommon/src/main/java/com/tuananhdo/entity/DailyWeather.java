package com.tuananhdo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Setter
@Getter
@Table(name = "weather_daily")
public class DailyWeather {
    @EmbeddedId
    private DailyWeatherId dailyWeatherId = new DailyWeatherId();
    private int minTemp;
    private int maxTemp;
    private int precipitation;
    @Column(length = 50)
    private String status;

    public DailyWeather getCoppyId() {
        DailyWeather coppy = new DailyWeather();
        coppy.setDailyWeatherId(this.getDailyWeatherId());
        return coppy;
    }

    public DailyWeather precipitation(int precipitation) {
        setPrecipitation(precipitation);
        return this;
    }

    public DailyWeather status(String status) {
        setStatus(status);
        return this;
    }

    public DailyWeather location(Location location) {
        this.dailyWeatherId.setLocation(location);
        return this;
    }

    public DailyWeather dayOfMonth(int day) {
        this.dailyWeatherId.setDayOfMonth(day);
        return this;
    }

    public DailyWeather month(int month) {
        this.dailyWeatherId.setMonth(month);
        return this;
    }

    public DailyWeather minTemp(int minTemp) {
        setMinTemp(minTemp);
        return this;
    }

    public DailyWeather maxTemp(int maxTemp) {
        setMaxTemp(maxTemp);
        return this;
    }

    @Override
    public String toString() {
        return "DailyWeather{" +
                "dailyWeatherId=" + dailyWeatherId +
                ", minTemp=" + minTemp +
                ", maxTemp=" + maxTemp +
                ", precipitation=" + precipitation +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWeather that = (DailyWeather) o;
        return Objects.equals(dailyWeatherId, that.dailyWeatherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dailyWeatherId);
    }
}
