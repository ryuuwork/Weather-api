package com.tuananhdo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.service.LocationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import payload.LocationDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationAPIController.class)
public class LocationAPIControllerTests {

    private static final String END_POINT_PATH = "/api/v1/locations";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    LocationService locationService;

    @Test
    public void testAddShouldReturn400BadRequest() throws Exception {
        LocationDTO location = new LocationDTO();
        String bodyContent = mapper.writeValueAsString(location);
        mockMvc.perform(post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testAddShouldReturn201Created() throws Exception {
        String code = "VN_DBP";
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCode(code);
        locationDTO.setCityName("Thanh Pho Dien Bien Phu");
        locationDTO.setRegionName("Dien Bien Phu");
        locationDTO.setCountryCode("DBP");
        locationDTO.setCountryName("Chien Dich Dien Bien Phu");
        locationDTO.setEnabled(true);

        Mockito.when(locationService.addLocation(locationDTO))
                .thenReturn(locationDTO);

        String bodyContent = mapper.writeValueAsString(locationDTO);
        mockMvc.perform(post(END_POINT_PATH)
                        .contentType("application/hal+json")
                        .content(bodyContent))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(header().string("Location", END_POINT_PATH + "/" + code))
                .andExpect(jsonPath("code", is("VN_DBP")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost" + END_POINT_PATH + "/" + code)))
                .andExpect(jsonPath("$._links.realtime_weather.href", is("http://localhost/api/v1/realtime/" + code)))
                .andExpect(jsonPath("$._links.hourly_weather.href", is("http://localhost/api/v1/hourly/" + code)))
                .andExpect(jsonPath("$._links.daily_weather.href", is("http://localhost/api/v1/daily/" + code)))
                .andExpect(jsonPath("$._links.full_weather.href", is("http://localhost/api/v1/full/" + code)))
                .andDo(print());
    }

    @Test
    @Disabled
    public void testListShouldReturn204NoContent() throws Exception {
        Mockito.when(locationService.findUntrashedLocation())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCode("VN_DBPX");
        locationDTO.setCityName("Thanh Pho Dien Bien Phu");
        locationDTO.setRegionName("Dien Bien Phu");
        locationDTO.setCountryCode("DBP");
        locationDTO.setCountryName("Chien Dich Dien Bien Phu");
        locationDTO.setEnabled(true);

        String code = locationDTO.getCode();
        Mockito.when(locationService.updateLocation(Mockito.eq(locationDTO), Mockito.eq(code)))
                .thenThrow(new LocationNotFoundException("No location found : " + code));

        String bodyContent = mapper.writeValueAsString(locationDTO);

        mockMvc.perform(put(END_POINT_PATH + "/" + code)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCode("VN_DBP");
        locationDTO.setCityName("Thanh Pho Dien Bien Phu UPDATE");
        locationDTO.setRegionName("Dien Bien Phu UPDATE");
        locationDTO.setCountryCode("DBP");
        locationDTO.setCountryName("Chien Dich Dien Bien Phu");
        locationDTO.setEnabled(true);

        String code = locationDTO.getCode();
        Mockito.when(locationService.updateLocation(Mockito.eq(locationDTO), Mockito.eq(code)))
                .thenReturn(locationDTO);

        String bodyContent = mapper.writeValueAsString(locationDTO);

        mockMvc.perform(put(END_POINT_PATH + "/" + code)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Location updated successfully"))
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn404NotFound() throws Exception {
        String code = "VN_DBPX";

        Mockito.doThrow(LocationNotFoundException.class).when(locationService)
                .deleteLocation(code);

        mockMvc.perform(delete(END_POINT_PATH + "/" + code)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn200OK() throws Exception {
        String code = "VN_DBPX";

        Mockito.doNothing().when(locationService).deleteLocation(code);

        mockMvc.perform(delete(END_POINT_PATH + "/" + code)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Location deleted successfully"))
                .andDo(print());
    }


    @Test
    public void testValidateRequestBodyLocationCodeLength() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCode("1");
        locationDTO.setCityName("Thanh Pho Dien Bien Phu UPDATE");
        locationDTO.setRegionName("Dien Bien Phu UPDATE");
        locationDTO.setCountryCode("DBP");
        locationDTO.setCountryName("Chien Dich Dien Bien Phu");
        locationDTO.setEnabled(true);

        String bodyContent = mapper.writeValueAsString(locationDTO);

        mockMvc.perform(post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0]", is("Location code must have 2-12 characters")))
                .andDo(print());
    }

    @Test
    public void testValidateRequestBodyAllFieldsInvalid() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        String bodyContent = mapper.writeValueAsString(locationDTO);

        mockMvc.perform(post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void testListByPageShouldReturn204NoContent() throws Exception {
        Mockito.when(locationService.listByPage(anyInt(), anyInt(), Mockito.anyString(), Mockito.anyMap())).thenReturn(Page.empty());
        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @Disabled
    public void testListByPageShouldReturn400BadRequestInvalidPageNumber() throws Exception {
        int pageNumber = 1;
        int pageSize = 5;
        String sortField = "code";

        Mockito.when(locationService.listByPage(pageNumber, pageSize, sortField))
                .thenReturn(Page.empty());

        String requestURL = END_POINT_PATH + "?page=" + pageNumber + "&size=" + pageSize + "&sort=" + sortField;

        mockMvc.perform(get(requestURL))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]", containsString("must be greater than or equal to 1")))
                .andDo(print());
    }

    @Test
    public void testListByPageShouldReturn400BadRequestInvalidSortField() throws Exception {
        int pageNumber = 1;
        int pageSize = 5;
        String sortField = "code_code";

        Mockito.when(locationService.listByPage(pageNumber, pageSize, sortField))
                .thenReturn(Page.empty());

        String requestURL = END_POINT_PATH + "?page=" + pageNumber + "&size=" + pageSize + "&sort=" + sortField;

        mockMvc.perform(get(requestURL))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]", containsString("No valid code found")))
                .andDo(print());
    }

    @Test
    public void testListPageShouldReturn200OK() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCode("VN_DBP");
        locationDTO.setCityName("Thanh Pho Dien Bien Phu");
        locationDTO.setRegionName("Dien Bien Phu");
        locationDTO.setCountryCode("DBP");
        locationDTO.setCountryName("Chien Dich Dien Bien Phu");
        locationDTO.setEnabled(true);
        locationDTO.setTrashed(true);

        LocationDTO locationDTO1 = new LocationDTO();
        locationDTO1.setCode("VN_DBP1");
        locationDTO1.setCityName("Thanh Pho Dien Bien Phu1");
        locationDTO1.setRegionName("Dien Bien Phu1");
        locationDTO1.setCountryCode("DBP1");
        locationDTO1.setCountryName("Chien Dich Dien Bien Phu1");
        locationDTO1.setEnabled(true);
        locationDTO1.setTrashed(false);

        LocationDTO locationDTO2 = new LocationDTO();
        locationDTO2.setCode("VN_DBP2");
        locationDTO2.setCityName("Thanh Pho Dien Bien Phu2");
        locationDTO2.setRegionName("Dien Bien Phu2");
        locationDTO2.setCountryCode("DBP2");
        locationDTO2.setCountryName("Chien Dich Dien Bien Phu2");
        locationDTO2.setEnabled(true);
        locationDTO2.setTrashed(true);

        List<LocationDTO> locationList = List.of(locationDTO, locationDTO1, locationDTO2);
        int pageNumber = 1;
        int pageSize = 5;
        String sortField = "code";
        int totalElements = locationList.size();

        Sort sort = Sort.by(sortField);
        Pageable pageable = PageRequest.of(0, pageSize, sort);

        Page<LocationDTO> page = new PageImpl<>(locationList, pageable, totalElements);
        Mockito.when(locationService.listByPage(anyInt(), anyInt(), anyString(), anyMap()))
                .thenReturn(page);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.locations[0].code", is("VN_DBP")))
                .andExpect(jsonPath("$.page.number", is(pageNumber)))
                .andExpect(jsonPath("$.page.size", is(pageSize)))
                .andExpect(jsonPath("$.page.total_elements", is(totalElements)))
                .andExpect(jsonPath("$.page.total_pages", is(1)))
                .andDo(print());
    }

    @Test
    public void testPaginationLinksOnlyOnePage() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCode("VN_DBP");
        locationDTO.setCityName("Thanh Pho Dien Bien Phu");
        locationDTO.setRegionName("Dien Bien Phu");
        locationDTO.setCountryCode("DBP");
        locationDTO.setCountryName("Chien Dich Dien Bien Phu");
        locationDTO.setTrashed(false);

        LocationDTO locationDTO1 = new LocationDTO();
        locationDTO1.setCode("VN_DBP1");
        locationDTO1.setCityName("Thanh Pho Dien Bien Phu1");
        locationDTO1.setRegionName("Dien Bien Phu1");
        locationDTO1.setCountryCode("DBP1");
        locationDTO1.setCountryName("Chien Dich Dien Bien Phu1");
        locationDTO1.setTrashed(false);

        List<LocationDTO> locationList = List.of(locationDTO, locationDTO1);
        int pageNumber = 1;
        int pageSize = 5;
        String sortField = "code";
        int totalElements = locationList.size();

        Sort sort = Sort.by(sortField);
        Pageable pageable = PageRequest.of(0, pageSize, sort);

        Page<LocationDTO> page = new PageImpl<>(locationList, pageable, totalElements);
        Mockito.when(locationService.listByPage(anyInt(), anyInt(), anyString(), anyMap())).thenReturn(page);

        String hostName = "http://localhost";
        String requestURL = END_POINT_PATH + "?page=" + pageNumber + "&size=" + pageSize + "&sort=" + sortField;

        mockMvc.perform(get(requestURL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href", containsString(hostName + requestURL)))
                .andExpect(jsonPath("$._links.first").doesNotExist())
                .andExpect(jsonPath("$._links.previous").doesNotExist())
                .andExpect(jsonPath("$._links.next").doesNotExist())
                .andExpect(jsonPath("$._links.last").doesNotExist())
                .andDo(print());
    }

    @Test
    public void testPaginationLinksInFirstPage() throws Exception {
        int totalElements = 18;
        int pageSize = 5;

        List<LocationDTO> locationDTOS = new ArrayList<>(pageSize);

        IntStream.range(1, 6).forEach(i ->
                locationDTOS.add(new LocationDTO("Code_" + i, "CityName_" + i, "RegionName_" + i, "CountryName_" + i, "counTryCode_" + i)));

        int pageNumber = 1;
        int totalPages = totalElements / pageSize + 1;
        String sortField = "code";
        Sort sort = Sort.by(sortField);
        Pageable pageable = PageRequest.of(0, pageSize, sort);
        Page<LocationDTO> page = new PageImpl<>(locationDTOS, pageable, totalElements);

        Mockito.when(locationService.listByPage(anyInt(), anyInt(), anyString(), anyMap())).thenReturn(page);

        String hostName = "http://localhost";
        String requestURL = END_POINT_PATH + "?page=" + pageNumber + "&size=" + pageSize + "&sort=" + sortField;
        String nextPageRequest = END_POINT_PATH + "?page=" + (pageNumber + 1) + "&size=" + pageSize + "&sort=" + sortField;
        String lastPageRequest = END_POINT_PATH + "?page=" + totalPages + "&size=" + pageSize + "&sort=" + sortField;

        mockMvc.perform(get(requestURL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links.first").doesNotExist())
                .andExpect(jsonPath("$._links.previous").doesNotExist())
                .andExpect(jsonPath("$._links.next.href", containsString(hostName + nextPageRequest)))
                .andExpect(jsonPath("$._links.last.href", containsString(hostName + lastPageRequest)))
                .andDo(print());
    }

}
