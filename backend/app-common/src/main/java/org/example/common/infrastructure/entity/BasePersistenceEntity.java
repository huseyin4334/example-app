package org.example.common.infrastructure.entity;

import java.util.Objects;

public abstract class BasePersistenceEntity<ID> {

    public abstract ID getId();
    public abstract void setId(ID id);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        BasePersistenceEntity<?> that = (BasePersistenceEntity<?>) obj;

        if (this.getId() == null && that.getId() != null) return false;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
