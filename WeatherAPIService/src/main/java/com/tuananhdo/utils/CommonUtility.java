package com.tuananhdo.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CommonUtility {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtility.class);
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    public static String getIPAddress(HttpServletRequest request) {
        String ipAddress = Optional
                .ofNullable(request.getHeader(X_FORWARDED_FOR))
                .filter(ip -> !ip.isEmpty())
                .orElse(request.getRemoteAddr());
        LOGGER.info("IP Address :" + ipAddress);
        return ipAddress;
    }

}
