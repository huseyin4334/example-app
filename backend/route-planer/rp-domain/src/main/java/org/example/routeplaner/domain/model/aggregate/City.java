package org.example.routeplaner.domain.model.aggregate;

import lombok.Getter;
import lombok.Setter;
import org.example.common.domain.entity.BaseEntity;

import java.util.Objects;

@Getter @Setter
public class City extends BaseEntity<Long> {
    private String name;
    private Country country;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof City city)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(name, city.name) && Objects.equals(country, city.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, country);
    }
}
