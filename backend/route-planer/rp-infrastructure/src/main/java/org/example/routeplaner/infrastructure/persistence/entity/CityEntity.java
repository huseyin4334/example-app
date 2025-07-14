package org.example.routeplaner.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.common.infrastructure.entity.BasePersistenceEntity;

@Getter @Setter
@Entity
@Table(name = "cities", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "country_id"}))
public class CityEntity extends BasePersistenceEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id", nullable = false, updatable = false)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity country;
}
