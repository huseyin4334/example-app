package org.example.routeplaner.infrastructure.persistence.repository;

import org.example.routeplaner.infrastructure.persistence.entity.CountryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CountryEntityRepositoryTest {

    @Autowired
    private CountryEntityRepository repository;

    @Test
    void shouldSaveAndFindCountry() {
        // Given
        CountryEntity country = new CountryEntity();
        country.setName("Turkey");
        country.setCode("TR");

        // When
        CountryEntity saved = repository.save(country);
        Optional<CountryEntity> found = repository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Turkey");
        assertThat(found.get().getCode()).isEqualTo("TR");
    }

    @Test
    void shouldFindAllCountries() {
        // Given
        CountryEntity turkey = new CountryEntity();
        turkey.setName("Turkey");
        turkey.setCode("TR");

        CountryEntity germany = new CountryEntity();
        germany.setName("Germany");
        germany.setCode("DE");

        repository.save(turkey);
        repository.save(germany);

        // When
        List<CountryEntity> all = repository.findAll();

        // Then
        assertThat(all).hasSize(2);
        assertThat(all).extracting(CountryEntity::getName)
                .containsExactlyInAnyOrder("Turkey", "Germany");
        assertThat(all).extracting(CountryEntity::getCode)
                .containsExactlyInAnyOrder("TR", "DE");
    }

    @Test
    void shouldDeleteCountry() {
        // Given
        CountryEntity country = new CountryEntity();
        country.setName("France");
        country.setCode("FR");
        CountryEntity saved = repository.save(country);

        // When
        repository.deleteById(saved.getId());

        // Then
        Optional<CountryEntity> found = repository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void shouldCountAllCountries() {
        // Given
        CountryEntity turkey = new CountryEntity();
        turkey.setName("Turkey");
        turkey.setCode("TR");

        CountryEntity germany = new CountryEntity();
        germany.setName("Germany");
        germany.setCode("DE");

        repository.save(turkey);
        repository.save(germany);

        // When
        long count = repository.count();

        // Then
        assertThat(count).isEqualTo(2);
    }
}

