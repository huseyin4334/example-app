package org.example.routeplaner.infrastructure.persistence.repository;

import org.example.routeplaner.infrastructure.persistence.entity.CityEntity;
import org.example.routeplaner.infrastructure.persistence.entity.CountryEntity;
import org.example.routeplaner.infrastructure.persistence.entity.LocationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LocationEntityRepositoryTest {

    @Autowired
    private LocationEntityRepository repository;

    @Autowired
    private CityEntityRepository cityRepository;

    @Autowired
    private CountryEntityRepository countryRepository;

    private CityEntity city;

    @BeforeEach
    void setUp() {
        CountryEntity country = new CountryEntity();
        country.setName("Turkey");
        country.setCode("TR");
        country = countryRepository.save(country);

        city = new CityEntity();
        city.setName("Istanbul");
        city.setCountry(country);
        city = cityRepository.save(city);
    }

    @Test
    void shouldSaveAndFindLocation() {
        // Given
        LocationEntity location = new LocationEntity();
        location.setName("Istanbul Airport");
        location.setLocationCode("IST");
        location.setCity(city);

        // When
        LocationEntity saved = repository.save(location);
        Optional<LocationEntity> found = repository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Istanbul Airport");
        assertThat(found.get().getLocationCode()).isEqualTo("IST");
        assertThat(found.get().getCity().getName()).isEqualTo("Istanbul");
    }

    @Test
    void shouldDeleteLocation() {
        // Given
        LocationEntity location = new LocationEntity();
        location.setName("Temp Location");
        location.setLocationCode("TMP");
        location.setCity(city);
        LocationEntity saved = repository.save(location);

        // When
        repository.deleteById(saved.getId());

        // Then
        Optional<LocationEntity> deleted = repository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    void shouldFindLocationsByNameLike() {
        // Given
        LocationEntity istanbul = createLocation("Istanbul", "IST");
        LocationEntity izmir = createLocation("Izmir", "IZM");
        LocationEntity ankara = createLocation("Ankara", "ANK");

        repository.save(istanbul);
        repository.save(izmir);
        repository.save(ankara);

        // When
        List<LocationEntity> found = repository.findByNameLikeOrLocationCodeLike(
                "%I%", "%I%", PageRequest.of(0, 10));

        // Then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(LocationEntity::getName)
                .containsExactlyInAnyOrder("Istanbul", "Izmir");
    }

    @Test
    void shouldFindLocationsByLocationCodeLike() {
        // Given
        LocationEntity istanbul = createLocation("Istanbul", "IST");
        LocationEntity izmir = createLocation("Izmir", "IZM");

        repository.save(istanbul);
        repository.save(izmir);

        // When
        List<LocationEntity> found = repository.findByNameLikeOrLocationCodeLike(
                "%nonexistent%", "%IS%", PageRequest.of(0, 10));

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getLocationCode()).isEqualTo("IST");
    }

    @Test
    void shouldCountLocationsByNameLikeOrLocationCodeLike() {
        // Given
        LocationEntity istanbul = createLocation("Istanbul", "IST");
        LocationEntity izmir = createLocation("Izmir", "IZM");
        LocationEntity ankara = createLocation("Ankara", "ANK");

        repository.save(istanbul);
        repository.save(izmir);
        repository.save(ankara);

        // When
        long count = repository.countByNameLikeOrLocationCodeLike("%I%", "%I%");

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void shouldReturnEmptyWhenNoMatches() {
        // Given
        LocationEntity ankara = createLocation("Ankara", "ANK");
        repository.save(ankara);

        // When
        List<LocationEntity> found = repository.findByNameLikeOrLocationCodeLike(
                "%XYZ%", "%XYZ%", PageRequest.of(0, 10));

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void shouldRespectPageableLimit() {
        // Given
        for (int i = 0; i < 5; i++) {
            LocationEntity location = createLocation("Location" + i, "LOC" + i);
            repository.save(location);
        }

        // When
        List<LocationEntity> found = repository.findByNameLikeOrLocationCodeLike(
                "%Location%", "%LOC%", PageRequest.of(0, 3));

        // Then
        assertThat(found).hasSize(3);
    }

    private LocationEntity createLocation(String name, String locationCode) {
        LocationEntity location = new LocationEntity();
        location.setName(name);
        location.setLocationCode(locationCode);
        location.setCity(city);
        return location;
    }
}
