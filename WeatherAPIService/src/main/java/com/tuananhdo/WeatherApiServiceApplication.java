package com.tuananhdo;

import com.tuananhdo.entity.DailyWeather;
import com.tuananhdo.entity.HourlyWeather;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import payload.DailyWeatherDTO;
import payload.HourlyWeatherDTO;

@SpringBootApplication()
    public class WeatherApiServiceApplication {
    @Bean
    public ModelMapper mapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        var typeMapHourlyWeather = modelMapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class);
        typeMapHourlyWeather.addMapping(hourlyWeather -> hourlyWeather.getWeatherId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);

        var typeMapHourlyWeatherDTO = modelMapper.typeMap(HourlyWeatherDTO.class, HourlyWeather.class);
        typeMapHourlyWeatherDTO.addMapping(HourlyWeatherDTO::getHourOfDay, (getHourlyWeatherId, value)
                -> getHourlyWeatherId.getWeatherId().setHourOfDay(value != null ? (int) value : 0));

        var typeMapDailyWeather = modelMapper.typeMap(DailyWeather.class, DailyWeatherDTO.class);
        typeMapDailyWeather.addMapping(dailyWeather -> dailyWeather.getDailyWeatherId().getDayOfMonth(), DailyWeatherDTO::setDayOfMonth);
        typeMapDailyWeather.addMapping(dailyWeather -> dailyWeather.getDailyWeatherId().getMonth(), DailyWeatherDTO::setMonth);
        return modelMapper;
    }
    public static void main(String[] args) {
        SpringApplication.run(WeatherApiServiceApplication.class, args);
    }

}
