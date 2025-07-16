package org.example.routeplaner.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.common.infrastructure.entity.BasePersistenceEntity;

@Getter @Setter
@Entity
@Table(name = "airport")
public class AirportEntity extends BasePersistenceEntity<Long> {
    @Id
    @SequenceGenerator(name = "airport_id_seq", sequenceName = "airport_id_seq", allocationSize = 5)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airport_id_seq")
    @Column(name = "airport_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;
}