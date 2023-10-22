package com.tuananhdo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tuananhdo.entity.DailyWeather;
import com.tuananhdo.entity.HourlyWeather;
import com.tuananhdo.entity.Location;
import com.tuananhdo.entity.RealtimeWeather;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import payload.DailyWeatherDTO;
import payload.FullWeatherDTO;
import payload.HourlyWeatherDTO;
import payload.RealtimeWeatherDTO;

@SpringBootApplication()
public class WeatherApiServiceApplication {
    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = configureMatchingStratery();
        configureMapping(mapper);
        return mapper;
    }

    private static ModelMapper configureMatchingStratery() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    private static void configureMapping(ModelMapper mapper) {
        configureMappingForHourlyWeather(mapper);
        configureMappingForDailyWeather(mapper);
        configureMappingForFullWeather(mapper);
        configureMappingForRealtimeWeather(mapper);
    }

    private static void configureMappingForRealtimeWeather(ModelMapper mapper) {
        mapper.typeMap(RealtimeWeatherDTO.class, RealtimeWeather.class)
                .addMappings(realtimeWeatherDTO -> realtimeWeatherDTO.skip(RealtimeWeather::setLocation));
    }

    private static void configureMappingForFullWeather(ModelMapper mapper) {
        mapper.typeMap(Location.class, FullWeatherDTO.class)
                .addMapping(Location::toString, FullWeatherDTO::setLocation);
    }

    private static void configureMappingForDailyWeather(ModelMapper mapper) {
        mapper.typeMap(DailyWeather.class, DailyWeatherDTO.class)
                .addMapping(dailyWeather -> dailyWeather.getDailyWeatherId().getDayOfMonth(), DailyWeatherDTO::setDayOfMonth)
                .addMapping(dailyWeather -> dailyWeather.getDailyWeatherId().getMonth(), DailyWeatherDTO::setMonth);

        mapper.typeMap(DailyWeatherDTO.class, DailyWeather.class)
                .addMapping(DailyWeatherDTO::getDayOfMonth,
                        (id, value) -> id.getDailyWeatherId().setDayOfMonth(value != null ? (int) value : 0))
                .addMapping(DailyWeatherDTO::getMonth,
                        (id, value) -> id.getDailyWeatherId().setMonth(value != null ? (int) value : 0));
    }

    private static void configureMappingForHourlyWeather(ModelMapper mapper) {
        mapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class)
                .addMapping(hourlyWeather -> hourlyWeather.getWeatherId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);

        mapper.typeMap(HourlyWeatherDTO.class, HourlyWeather.class)
                .addMapping(HourlyWeatherDTO::getHourOfDay,
                        (id, value) -> id.getWeatherId().setHourOfDay(value != null ? (int) value : 0));
    }

    @Bean
    ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiServiceApplication.class, args);
    }

}
