package org.example.routeplaner.domain.model.aggregate;

import lombok.Getter;
import lombok.Setter;
import org.example.common.domain.entity.BaseEntity;

import java.util.Objects;

@Getter @Setter
public class Country extends BaseEntity<Long> {
    private String code;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Country country)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(code, country.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), code);
    }
}
