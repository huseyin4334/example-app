package org.example.routeplaner.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.common.infrastructure.entity.BasePersistenceEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "locations", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "city", "country"}))
public class LocationEntity extends BasePersistenceEntity<UUID> {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;

    @Column(name = "location_code", length = 16, unique = true, nullable = false)
    private String locationCode;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LocationEntity that)) return false;
        return Objects.equals(locationCode, that.locationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationCode);
    }
}
