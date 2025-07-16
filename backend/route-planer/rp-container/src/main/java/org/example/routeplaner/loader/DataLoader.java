package org.example.routeplaner.loader;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.common.TransportConstants;
import org.example.routeplaner.infrastructure.persistence.entity.AirportEntity;
import org.example.routeplaner.infrastructure.persistence.entity.CityEntity;
import org.example.routeplaner.infrastructure.persistence.entity.CountryEntity;
import org.example.routeplaner.infrastructure.persistence.entity.TransportationTypeEntity;
import org.example.routeplaner.infrastructure.persistence.repository.AirportEntityRepository;
import org.example.routeplaner.infrastructure.persistence.repository.CityEntityRepository;
import org.example.routeplaner.infrastructure.persistence.repository.CountryEntityRepository;
import org.example.routeplaner.infrastructure.persistence.repository.TransportationTypeEntityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Profile("!test")
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final CountryEntityRepository countryEntityRepository;
    private final CityEntityRepository cityEntityRepository;
    private final AirportEntityRepository airportEntityRepository;
    private final TransportationTypeEntityRepository transportationTypeEntityRepository;
    private final ObjectMapper objectMapper;

    private Map<String, CityEntity> cityEntities = new HashMap<>();
    private Map<String, CountryEntity> countryEntities = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        if (airportEntityRepository.count() != 0)
            return;

        objectMapper
                .findAndRegisterModules()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
                .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

        // Load countries and cities from JSON files
        loadCountries();

        // Load airports from JSON files
        loadAirportsAndCities();

        // Load transportation types
        loadTransportationTypes();

        // Clear Local Maps
        cityEntities = null;
        countryEntities = null;
    }

    private void loadTransportationTypes() {
        TransportationTypeEntity type = new TransportationTypeEntity();
        type.setName("BUS");
        transportationTypeEntityRepository.save(type);

        type = new TransportationTypeEntity();
        type.setName("TRAIN");
        transportationTypeEntityRepository.save(type);

        type = new TransportationTypeEntity();
        type.setName(TransportConstants.FLIGHT);
        transportationTypeEntityRepository.save(type);

        type = new TransportationTypeEntity();
        type.setName("UBER");
        transportationTypeEntityRepository.save(type);
    }

    private void loadAirportsAndCities() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("data/airports.json");
        InputStream inputStream = classPathResource.getInputStream();

        Map<String, Airport> airportMap = objectMapper.readValue(inputStream, new TypeReference<>() {});

        List<AirportEntity> airportEntities = new ArrayList<>();
        for (Airport airport : airportMap.values()) {
            if (airport.getIcao() == null || airport.getIcao().isEmpty())
                continue;

            CityEntity cityEntity = cityEntities.get(airport.getCity());
            if (cityEntity == null) {
                CountryEntity countryEntity = countryEntities.get(airport.getCountry());
                if (countryEntity == null)
                    continue;

                cityEntity = new CityEntity();
                cityEntity.setName(airport.getCity());
                cityEntity.setCountry(countryEntity);
                cityEntities.put(airport.getCity(), cityEntity);
            }

            AirportEntity airportEntity = new AirportEntity();
            airportEntity.setName(airport.getName());
            airportEntity.setCity(cityEntity);
            airportEntity.setCode(airport.getIcao());
            airportEntities.add(airportEntity);
        }

        cityEntityRepository.saveAll(cityEntities.values());
        airportEntityRepository.saveAll(airportEntities);

        System.out.println("Loaded cities: " + cityEntities.size());
        System.out.println("Loaded airports: " + airportMap.size());
    }

    private void loadCountries() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("data/countries.json");
        InputStream inputStream = classPathResource.getInputStream();

        List<Map<String, String>> countries = objectMapper.readValue(inputStream, new TypeReference<>() {});

        for(Map<String, String> country : countries) {
            CountryEntity countryEntity = new CountryEntity();
            countryEntity.setName(country.get("name"));
            countryEntity.setCode(country.get("code"));
            countryEntityRepository.save(countryEntity);
            countryEntities.put(countryEntity.getCode(), countryEntity);
        }

        countryEntityRepository.saveAll(countryEntities.values());

        System.out.println("Loaded countries: " + countries.size());
    }
}
