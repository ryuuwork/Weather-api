package com.tuananhdo.utils;

public enum SortField {
    CODE("code"),
    CITY_NAME("cityName"),
    REGION_NAME("regionName"),
    COUNTRY_CODE("countryCode"),
    COUNTRY_NAME("countryName"),
    ENABLED("enabled");

    private final String fieldName;

    SortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
