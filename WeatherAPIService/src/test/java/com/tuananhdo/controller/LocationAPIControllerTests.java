package com.tuananhdo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuananhdo.exception.LocationNotFoundException;
import com.tuananhdo.service.LocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import payload.LocationDTO;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
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
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCode("VN_DBP");
        locationDTO.setCityName("Thanh Pho Dien Bien Phu");
        locationDTO.setRegionName("Dien Bien Phu");
        locationDTO.setCountryCode("DBP");
        locationDTO.setCountryName("Chien Dich Dien Bien Phu");
        locationDTO.setEnabled(true);

        Mockito.when(locationService.addLocation(locationDTO))
                .thenReturn(locationDTO);

        String bodyContent = mapper.writeValueAsString(locationDTO);
        mockMvc.perform(post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("code", is("VN_DBP")))
                .andExpect(header().string("Location", "/api/v1/locations/VN_DBP"))
                .andDo(print());
    }

    @Test
    public void testListShouldReturn204NoContent() throws Exception {
        Mockito.when(locationService.findUntrashedLocation())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testListShouldReturn20OK() throws Exception {
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

        Mockito.when(locationService.findUntrashedLocation())
                .thenReturn(List.of(locationDTO, locationDTO1, locationDTO2));

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].code", is("VN_DBP")))
                .andExpect(jsonPath("$[1].code", is("VN_DBP1")))
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

}
