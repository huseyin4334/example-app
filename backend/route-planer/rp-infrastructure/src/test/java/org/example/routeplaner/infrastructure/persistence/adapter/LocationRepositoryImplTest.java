package org.example.routeplaner.infrastructure.persistence.adapter;

import org.example.routeplaner.domain.model.aggregate.Location;
import org.example.routeplaner.infrastructure.persistence.entity.CityEntity;
import org.example.routeplaner.infrastructure.persistence.entity.CountryEntity;
import org.example.routeplaner.infrastructure.persistence.mapper.RegionEntitiesMapper;
import org.example.routeplaner.infrastructure.persistence.repository.CityEntityRepository;
import org.example.routeplaner.infrastructure.persistence.repository.CountryEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LocationRepositoryImplTest {

    @Autowired
    LocationRepositoryImpl locationRepository;

    @Autowired
    CityEntityRepository cityEntityRepository;

    @Autowired
    CountryEntityRepository countryEntityRepository;

    @Autowired
    RegionEntitiesMapper regionEntitiesMapper;

    Location location;
    CountryEntity country;

    @BeforeEach
    void setUp() {
        country = new CountryEntity();
        country.setCode("TR");
        country.setName("TestCountryName");
        CityEntity city = new CityEntity();
        city.setName("TestCityName");
        city.setCountry(country);

        countryEntityRepository.save(country);
        cityEntityRepository.save(city);

        location = new Location();
        location.setName("TestName");
        location.setCity(regionEntitiesMapper.toCity(city));
        location.setLocationCode("LOC123");
        locationRepository.save(location);
    }

    @Test
    void testFindById() {
        Location found = locationRepository.findById(location.getId());
        assertNotNull(found);
        assertEquals(location.getName(), found.getName());
    }

    @Test
    void testPage() {
        List<Location> page = locationRepository.page(0, 10);
        assertFalse(page.isEmpty());
        assertTrue(page.stream().anyMatch(l -> l.getId().equals(location.getId())));
    }

    @Test
    void testSearch() {
        List<Location> results = locationRepository.search("Test", 0, 10);
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(l -> l.getName().equals("TestName")));
    }

    @Test
    void testCount() {
        long count = locationRepository.count("%Test%");
        assertTrue(count > 0);
    }

    @Test
    void testUpdate() {
        CityEntity city = new CityEntity();
        city.setName("UpdatedCity");
        city.setCountry(country);
        location.setName("UpdatedCity");
        cityEntityRepository.save(city);

        location.setCity(regionEntitiesMapper.toCity(city));
        locationRepository.update(location);
        Location updated = locationRepository.findById(location.getId());
        CityEntity cityEntity = cityEntityRepository.findByLocationId(updated.getId());
        assertEquals("UpdatedCity", updated.getName());
        assertEquals(city.getName(), cityEntity.getName());
    }

    @Test
    void testDeleteById() {
        locationRepository.deleteById(location.getId());
        Location deleted = locationRepository.findById(location.getId());
        assertNull(deleted);
    }
}
