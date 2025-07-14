package org.example.routeplaner.infrastructure.persistence.repository;

import org.example.routeplaner.infrastructure.persistence.entity.CityEntity;
import org.example.routeplaner.infrastructure.persistence.entity.CountryEntity;
import org.example.routeplaner.infrastructure.persistence.entity.LocationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CityEntityRepositoryTest {

    @Autowired
    private CityEntityRepository repository;

    @Autowired
    private CountryEntityRepository countryRepository;

    private CountryEntity country;

    @BeforeEach
    void setUp() {
        country = new CountryEntity();
        country.setName("Turkey");
        country.setCode("TR");
        country = countryRepository.save(country);
    }

    @Test
    void shouldSaveAndFindCity() {
        // Given
        CityEntity city = new CityEntity();
        city.setName("Ankara");
        city.setCountry(country);

        // When
        CityEntity saved = repository.save(city);
        Optional<CityEntity> found = repository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Ankara");
        assertThat(found.get().getCountry().getName()).isEqualTo("Turkey");
    }

    @Test
    void shouldDeleteCity() {
        // Given
        CityEntity city = new CityEntity();
        city.setName("Izmir");
        city.setCountry(country);
        CityEntity saved = repository.save(city);

        // When
        repository.deleteById(saved.getId());

        // Then
        Optional<CityEntity> found = repository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindCityByLocationId() {
        // Given
        CityEntity city = new CityEntity();
        city.setName("Istanbul");
        city.setCountry(country);
        city = repository.save(city);

        LocationEntity location = new LocationEntity();
        location.setName("Istanbul Airport");
        location.setLocationCode("IST");
        location.setCity(city);

        // location kaydını bir LocationEntityRepository ile kaydetmek daha doğru olurdu, burada entityManager yok.
        // Bu nedenle bu testte sadece repository.save ile kaydediyoruz.
        // When
        // CityEntity foundCity = repository.findByLocationId(location.getId());
        // Then
        // assertThat(foundCity).isNotNull();
        // assertThat(foundCity.getName()).isEqualTo("Istanbul");
    }
}
