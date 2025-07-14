package org.example.routeplaner.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.common.infrastructure.entity.BasePersistenceEntity;

@Getter @Setter
@Entity
@Table(name = "countries")
public class CountryEntity extends BasePersistenceEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id", nullable = false, updatable = false)
    private Long id;

    // Assuming the country entity has a name and a code, you can add more fields as needed
    private String name;

    @Column(name = "country_code", length = 4, unique = true, nullable = false)
    private String code;
}
