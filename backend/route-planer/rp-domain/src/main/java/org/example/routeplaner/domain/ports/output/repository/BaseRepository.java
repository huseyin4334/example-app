package org.example.routeplaner.domain.ports.output.repository;

import org.example.common.domain.entity.BaseEntity;

import java.util.List;

public interface BaseRepository<T extends BaseEntity<ID>, ID> {

    List<T> search(String search, int page, int pageSize);

    List<T> page(int page, int size);

    T findById(ID id);

    void save(T entity);

    void deleteById(ID entity);

    void update(T entity);

    long count(String search);
}
