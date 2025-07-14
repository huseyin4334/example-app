package org.example.routeplaner.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.common.domain.entity.Day;
import org.example.routeplaner.application.dto.command.location.LocationCreateCommand;
import org.example.routeplaner.application.dto.command.route.AvailableRoutesQuery;
import org.example.routeplaner.domain.ports.output.repository.LocationRepository;
import org.example.routeplaner.infrastructure.persistence.entity.CityEntity;
import org.example.routeplaner.infrastructure.persistence.entity.CountryEntity;
import org.example.routeplaner.infrastructure.persistence.entity.TransportationTypeEntity;
import org.example.routeplaner.infrastructure.persistence.repository.CityEntityRepository;
import org.example.routeplaner.infrastructure.persistence.repository.CountryEntityRepository;
import org.example.routeplaner.infrastructure.persistence.repository.TransportationTypeEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FullRoutePlannerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    CityEntityRepository cityEntityRepository;

    @Autowired
    CountryEntityRepository countryEntityRepository;

    @Autowired
    TransportationTypeEntityRepository transportationTypeEntityRepository;

    @Autowired
    ObjectMapper objectMapper;

    String originId;
    String airportAId;
    String airportBId;
    String destinationId;
    Map<String, Long> cityIdList = new HashMap<>();
    Map<String, Long> transportationTypeList = new HashMap<>();

    @BeforeEach
    void setUp() throws Exception {
        if (originId != null)
            return;


        // Create locations
        createCityAndCountries();
        createTransportationTypes();

        originId = createLocation("Origin City", "ORI");
        airportAId = createLocation("Airport A", "ANK");
        airportBId = createLocation("Airport B", "IZM");
        destinationId = createLocation("Destination City", "ANT");

        // Create transportations with type IDs instead of enum values
        // Origin -> AirportA (BUS, MONDAY, FRIDAY)
        createTransportation(originId, airportAId, cityIdList.get("Istanbul"), Arrays.asList(Day.MONDAY, Day.FRIDAY), transportationTypeList.get("BUS"));
        // AirportA -> AirportB (FLIGHT, MONDAY)
        createTransportation(airportAId, airportBId, cityIdList.get("Ankara"), List.of(Day.MONDAY), transportationTypeList.get("FLIGHT"));
        // AirportB -> Destination (BUS, MONDAY, FRIDAY)
        createTransportation(airportBId, destinationId, cityIdList.get("Istanbul"), Arrays.asList(Day.MONDAY, Day.FRIDAY), transportationTypeList.get("BUS"));

        // Origin -> AirportB (BUS, FRIDAY)
        createTransportation(originId, airportBId, cityIdList.get("Istanbul"), List.of(Day.FRIDAY), transportationTypeList.get("BUS"));
        // AirportA -> Destination (BUS, MONDAY)
        createTransportation(airportAId, destinationId, cityIdList.get("Istanbul"), List.of(Day.MONDAY), transportationTypeList.get("BUS"));
    }

    private void createTransportationTypes() {
        TransportationTypeEntity type = new TransportationTypeEntity();
        type.setName("BUS");
        transportationTypeEntityRepository.save(type);
        transportationTypeList.put("BUS", type.getId());

        type = new TransportationTypeEntity();
        type.setName("FLIGHT");
        transportationTypeEntityRepository.save(type);
        transportationTypeList.put("FLIGHT", type.getId());

        type = new TransportationTypeEntity();
        type.setName("UBER");
        transportationTypeEntityRepository.save(type);
        transportationTypeList.put("UBER", type.getId());
    }

    private void createCityAndCountries() {
        CountryEntity country = new CountryEntity();
        country.setName("Turkey");
        country.setCode("TR");
        countryEntityRepository.save(country);

        CityEntity city = new CityEntity();
        city.setName("Istanbul");
        city.setCountry(country);
        cityEntityRepository.save(city);
        cityIdList.put("Istanbul", city.getId());

        city = new CityEntity();
        city.setName("Ankara");
        city.setCountry(country);
        cityEntityRepository.save(city);
        cityIdList.put("Ankara", city.getId());
    }

    String createLocation(String name, String code) throws Exception {
        LocationCreateCommand cmd = new LocationCreateCommand(null, name, 1L, code);
        mockMvc.perform(put("/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isOk());

        return locationRepository.search(name, 0, 10).stream()
                .filter(location -> location.getName().equals(name))
                .findFirst()
                .map(location -> location.getId().toString())
                .orElseThrow(() -> new RuntimeException("Location not found: " + name));
    }

    void createTransportation(String from, String to, Long type, List<Day> days, long transportationTypeId) throws Exception {
        // Manuel olarak JSON oluştur
        String json = String.format(
            "{\"origin\":\"%s\",\"destination\":\"%s\",\"transportationType\":%d,\"availableDays\":%s}",
            from, to, type, objectMapper.writeValueAsString(days)
        );

        mockMvc.perform(put("/transportations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testRouteCalculationOnMonday() throws Exception {
        // --- MONDAY: Route olmalı (Origin->AirportA->AirportB->Destination)
        AvailableRoutesQuery queryMonday = new AvailableRoutesQuery();
        queryMonday.setOriginId(originId);
        queryMonday.setDestinationId(destinationId);
        queryMonday.setSelectedDate(new java.util.Date());

        mockMvc.perform(post("/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queryMonday)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableRoutes").isNotEmpty());
    }

    @Test
    void testRouteCalculationOnFriday() throws Exception {
        // --- FRIDAY: Route olmalı (Origin->AirportB->Destination)
        AvailableRoutesQuery queryFriday = new AvailableRoutesQuery();
        queryFriday.setOriginId(originId);
        queryFriday.setDestinationId(destinationId);
        queryFriday.setSelectedDate(new java.util.Date());

        mockMvc.perform(post("/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queryFriday)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableRoutes").isNotEmpty());
    }

    @Test
    void testRouteCalculationOnSunday() throws Exception {
        // --- SUNDAY: Route olmamalı
        AvailableRoutesQuery querySunday = new AvailableRoutesQuery();
        querySunday.setOriginId(originId);
        querySunday.setDestinationId(destinationId);
        // 13.07.2025
        Date date = Date.from(LocalDateTime.of(2025, 7, 13, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        querySunday.setSelectedDate(date);

        mockMvc.perform(post("/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(querySunday)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableRoutes").isEmpty());
    }
}
