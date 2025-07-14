package org.example.routeplaner.domain.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.common.domain.entity.BaseEntity;

import java.util.Objects;

@Getter
@Setter
public class TransportationType extends BaseEntity<Long> {
    private String name;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TransportationType that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
