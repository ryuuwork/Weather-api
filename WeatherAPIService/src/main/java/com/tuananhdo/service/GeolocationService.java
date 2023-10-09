package com.tuananhdo.service;

import com.tuananhdo.exception.GeolocationException;
import payload.LocationDTO;

import java.io.IOException;

public interface GeolocationService {

    LocationDTO getLocation(String ipAddress) throws IOException, GeolocationException;
}
