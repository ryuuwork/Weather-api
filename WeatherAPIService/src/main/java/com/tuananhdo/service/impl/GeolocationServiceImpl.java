package com.tuananhdo.service.impl;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.tuananhdo.exception.GeolocationException;
import com.tuananhdo.service.GeolocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import payload.LocationDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
public class GeolocationServiceImpl implements GeolocationService {
    public static final String OK_STATUS = "OK";
    private static final Logger LOGGER = LoggerFactory.getLogger(GeolocationServiceImpl.class);
    private static final String DATABASE_PATH = "/ipweatherdb/IP2LOCATION-LITE-DB3.BIN";
    private final IP2Location ip2Location = new IP2Location();

    public GeolocationServiceImpl() throws GeolocationException {
        try (InputStream inputStream = GeolocationServiceImpl.class.getResourceAsStream(DATABASE_PATH)) {
            if (Objects.nonNull(inputStream)) {
                byte[] data = inputStream.readAllBytes();
                ip2Location.Open(data);
            } else {
                throw new GeolocationException("Could not find path of file IP2Location");
            }
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage(), exception);
            throw new GeolocationException("Error initializing IP2Location");
        }
    }
    @Override
    public LocationDTO getLocation(String ipAddress) throws IOException, GeolocationException {
        IPResult ipResult = ip2Location.IPQuery(ipAddress);
        String status = ipResult.getStatus();
        if (!OK_STATUS.equals(status)) {
            throw new GeolocationException("Geolocation failed with status: " + ipResult.getStatus());
        }
        return new LocationDTO(ipResult.getCity(), ipResult.getRegion(), ipResult.getCountryLong(), ipResult.getCountryShort());
    }

}
