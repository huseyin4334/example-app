package org.example.routeplaner.infrastructure.persistence.repository;

import org.example.routeplaner.infrastructure.persistence.entity.TransportationTypeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransportationTypeEntityRepositoryTest {

    @Autowired
    private TransportationTypeEntityRepository repository;

    @Test
    void shouldSaveAndFindTransportationType() {
        // Given
        TransportationTypeEntity entity = new TransportationTypeEntity();
        entity.setName("Bus");

        // When
        TransportationTypeEntity saved = repository.save(entity);
        Optional<TransportationTypeEntity> found = repository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Bus");
    }

    @Test
    void shouldFindAllTransportationTypes() {
        // Given
        TransportationTypeEntity bus = new TransportationTypeEntity();
        bus.setName("Bus");
        TransportationTypeEntity train = new TransportationTypeEntity();
        train.setName("Train");

        repository.save(bus);
        repository.save(train);

        // When
        List<TransportationTypeEntity> all = repository.findAll();

        // Then
        assertThat(all).hasSize(2);
        assertThat(all).extracting(TransportationTypeEntity::getName)
                .containsExactlyInAnyOrder("Bus", "Train");
    }

    @Test
    void shouldDeleteTransportationType() {
        // Given
        TransportationTypeEntity entity = new TransportationTypeEntity();
        entity.setName("Plane");
        TransportationTypeEntity saved = repository.save(entity);

        // When
        repository.deleteById(saved.getId());

        // Then
        Optional<TransportationTypeEntity> found = repository.findById(saved.getId());
        assertThat(found).isEmpty();
    }
}
