package com.tuananhdo.exception;


public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String locationCode) {
        super("Not found location: " + locationCode);
    }

    public LocationNotFoundException(String countryCode, String cityName) {
        super("Not found location with the given country code: " + countryCode + " and city name: " + cityName);
    }
}
