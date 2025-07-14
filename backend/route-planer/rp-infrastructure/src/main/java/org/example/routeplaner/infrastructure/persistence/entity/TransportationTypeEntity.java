package org.example.routeplaner.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.common.infrastructure.entity.BasePersistenceEntity;

@Getter @Setter
@Entity
@Table(name = "transportation_type")
public class TransportationTypeEntity extends BasePersistenceEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transportation_type_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
