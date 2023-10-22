package com.tuananhdo.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RootBaseURL {
    private String locationUrl;
    private String locationByCodeUrl;
    private String realtimeWeatherByIpAdressUrl;
    private String realtimeWeatherByCodeUrl;
    private String hourlyForecastByIpAddressUrl;
    private String hourlyForecastByCodeUrl;
    private String dailyForecastByIpAddressUrl;
    private String dailyForecastByCodeUrl;
    private String fullForecastByIpAddressUrl;
    private String fullForecastByCodeUrl;
}
