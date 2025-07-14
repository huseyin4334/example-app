package org.example.routeplaner.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.common.domain.entity.Day;
import org.example.common.infrastructure.entity.BasePersistenceEntity;
import org.hibernate.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "transportation", uniqueConstraints = @UniqueConstraint(columnNames = {"from_location_id", "to_location_id", "transportation_type"}))
public class TransportationEntity extends BasePersistenceEntity<UUID> {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @GeneratedValue
    @Column(name = "transportation_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "from_location_id", nullable = false, updatable = false)
    private LocationEntity fromLocation;

    @ManyToOne
    @JoinColumn(name = "to_location_id", nullable = false, updatable = false)
    private LocationEntity toLocation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transportation_type", nullable = false)
    private TransportationTypeEntity transportationType;

    @ElementCollection(targetClass = Day.class, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(
        name = "available_days",
        joinColumns = @JoinColumn(name = "transportation_id", nullable = false)
    )
    @Enumerated(EnumType.STRING)
    private List<Day> availableDays;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
