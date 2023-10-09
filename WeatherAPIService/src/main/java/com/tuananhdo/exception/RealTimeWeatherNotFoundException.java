package com.tuananhdo.exception;


public class RealTimeWeatherNotFoundException extends RuntimeException {
    public RealTimeWeatherNotFoundException(String message) {
        super(message);
    }
}
