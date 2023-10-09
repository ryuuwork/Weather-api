package com.tuananhdo.service;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class IP2LocationTests {

    private static final String databasePath = "ipweatherdb/IP2LOCATION-LITE-DB3.BIN";

    @Test
    public void testInvalidIPAddress() throws IOException {
        IP2Location ipAddress = new IP2Location();
        ipAddress.Open(databasePath);
        String ipAddressName = "QWERTXZZ";
        IPResult ipResult = ipAddress.IPQuery(ipAddressName);
        assertThat(ipResult.getStatus()).isEqualTo("INVALID_IP_ADDRESS");
        System.out.println(ipResult);
    }
    @Test
    public void testValidIPAddressJapan() throws IOException {
        IP2Location ipAddress = new IP2Location();
        ipAddress.Open(databasePath);
        String ipAddressName = "103.160.48.0";
        IPResult ipResult = ipAddress.IPQuery(ipAddressName);
        assertThat(ipResult.getStatus()).isEqualTo("OK");
        assertThat(ipResult.getRegion()).isEqualTo("Kanagawa");
        System.out.println(ipResult);
    }
    @Test
    public void testValidIPAddressNewYork() throws IOException {
        IP2Location ipAddress = new IP2Location();
        ipAddress.Open(databasePath);
        String ipAddressName = "128.59.59.218";
        IPResult ipResult = ipAddress.IPQuery(ipAddressName);
        assertThat(ipResult.getStatus()).isEqualTo("OK");
        System.out.println(ipResult);
    }

    @Test
    public void testValidIPAddressDienBienVN() throws IOException {
        IP2Location ipAddress = new IP2Location();
        ipAddress.Open(databasePath);
        String ipAddressName = "222.255.240.238";
        IPResult ipResult = ipAddress.IPQuery(ipAddressName);
        assertThat(ipResult.getStatus()).isEqualTo("OK");
        System.out.println(ipResult);
    }
}
