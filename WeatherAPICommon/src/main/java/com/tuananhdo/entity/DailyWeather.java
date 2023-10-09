package com.tuananhdo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
    public DailyWeather precipitation(int precipitation){
        setPrecipitation(precipitation);
        return this;
    }

    public DailyWeather status(String status){
        setStatus(status);
        return this;
    }

    public DailyWeather location(Location location){
        this.dailyWeatherId.setLocation(location);
        return this;
    }

    public DailyWeather dayOfMonth(int day){
        this.dailyWeatherId.setDayOfMonth(day);
        return this;
    }

    public DailyWeather month(int month){
        this.dailyWeatherId.setMonth(month);
        return this;
    }

    public DailyWeather minTemp(int minTemp){
        setMinTemp(minTemp);
        return this;
    }

    public DailyWeather maxTemp(int maxTemp){
        setMaxTemp(maxTemp);
        return this;
    }

}
