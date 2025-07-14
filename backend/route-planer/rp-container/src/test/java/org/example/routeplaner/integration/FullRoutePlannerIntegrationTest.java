package org.example.routeplaner.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.common.domain.entity.Day;
import org.example.routeplaner.application.dto.command.location.LocationCreateCommand;
import org.example.routeplaner.application.dto.command.route.AvailableRoutesQuery;
import org.example.routeplaner.domain.ports.output.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FullRoutePlannerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ObjectMapper objectMapper;

    String originId;
    String airportAId;
    String airportBId;
    String destinationId;

    @BeforeEach
    void setUp() throws Exception {
        // Create locations
        originId = createLocation("Origin City", "Turkey", "Istanbul", "ORI");
        airportAId = createLocation("Airport A", "Turkey", "Ankara", "ANK");
        airportBId = createLocation("Airport B", "Turkey", "Izmir", "IZM");
        destinationId = createLocation("Destination City", "Turkey", "Antalya", "ANT");

        // Create transportations with type IDs instead of enum values
        // Origin -> AirportA (BUS, MONDAY, FRIDAY)
        createTransportation(originId, airportAId, 1L, Arrays.asList(Day.MONDAY, Day.FRIDAY));
        // AirportA -> AirportB (FLIGHT, MONDAY)
        createTransportation(airportAId, airportBId, 2L, List.of(Day.MONDAY));
        // AirportB -> Destination (BUS, MONDAY, FRIDAY)
        createTransportation(airportBId, destinationId, 1L, Arrays.asList(Day.MONDAY, Day.FRIDAY));

        // Origin -> AirportB (BUS, FRIDAY)
        createTransportation(originId, airportBId, 1L, List.of(Day.FRIDAY));
        // AirportA -> Destination (BUS, MONDAY)
        createTransportation(airportAId, destinationId, 1L, List.of(Day.MONDAY));
    }

    String createLocation(String name, String country, String city, String code) throws Exception {
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

    void createTransportation(String from, String to, Long type, List<Day> days) throws Exception {
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

        mockMvc.perform(post("/routes/available")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queryMonday)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routes").isNotEmpty());
    }

    @Test
    void testRouteCalculationOnFriday() throws Exception {
        // --- FRIDAY: Route olmalı (Origin->AirportB->Destination)
        AvailableRoutesQuery queryFriday = new AvailableRoutesQuery();
        queryFriday.setOriginId(originId);
        queryFriday.setDestinationId(destinationId);
        queryFriday.setSelectedDate(new java.util.Date());

        mockMvc.perform(post("/routes/available")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queryFriday)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routes").isNotEmpty());
    }

    @Test
    void testRouteCalculationOnSunday() throws Exception {
        // --- SUNDAY: Route olmamalı
        AvailableRoutesQuery querySunday = new AvailableRoutesQuery();
        querySunday.setOriginId(originId);
        querySunday.setDestinationId(destinationId);
        querySunday.setSelectedDate(new java.util.Date());

        mockMvc.perform(post("/routes/available")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(querySunday)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routes").isEmpty());
    }
}
