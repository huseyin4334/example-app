package org.example.routeplaner.infrastructure.persistence.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.example.routeplaner.domain.model.aggregate.Transportation;
import org.example.routeplaner.domain.model.aggregate.TransportationType;
import org.example.routeplaner.domain.model.criteria.LocationCriteria;
import org.example.routeplaner.domain.ports.output.repository.TransportationRepository;
import org.example.routeplaner.infrastructure.persistence.entity.TransportationEntity;
import org.example.routeplaner.infrastructure.persistence.mapper.TransportationEntityMapper;
import org.example.routeplaner.infrastructure.persistence.repository.TransportationEntityRepository;
import org.example.routeplaner.infrastructure.persistence.repository.TransportationTypeEntityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class TransportationRepositoryImpl implements TransportationRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final TransportationEntityRepository transportationEntityRepository;
    private final TransportationEntityMapper transportationEntityMapper;
    private final TransportationTypeEntityRepository transportationTypeEntityRepository;


    @Override
    public List<Transportation> getOriginsByCriteria(LocationCriteria locationCriteria) {
        StringBuilder sql = new StringBuilder("SELECT t FROM TransportationEntity t JOIN t.fromLocation tf ");
        StringBuilder whereClause = new StringBuilder("WHERE 1=1 ");

        if (CollectionUtils.isNotEmpty(locationCriteria.getDestinationIdList())) {
            sql.append("JOIN t.toLocation tt ");
            whereClause.append("AND tt.id IN :destinationIdList ");
        }
        if (CollectionUtils.isNotEmpty(locationCriteria.getOriginIdList())) {
            whereClause.append("AND tf.id IN :originIdList ");
        }

        whereClauseByCriteria(locationCriteria, whereClause, sql);
        sql.append(whereClause);

        return getTransportations(locationCriteria, sql);
    }

    @Override
    public List<Transportation> getDestinationsByCriteria(LocationCriteria locationCriteria) {
        StringBuilder sql = new StringBuilder("SELECT t FROM TransportationEntity t JOIN t.toLocation tt ");
        StringBuilder whereClause = new StringBuilder("WHERE 1=1 ");

        if (CollectionUtils.isNotEmpty(locationCriteria.getDestinationIdList())) {
            whereClause.append("AND tt.id IN :destinationIdList ");
        }
        if (CollectionUtils.isNotEmpty(locationCriteria.getOriginIdList())) {
            sql.append("JOIN t.fromLocation tf ");
            whereClause.append("AND tf.id IN :originIdList ");
        }

        whereClauseByCriteria(locationCriteria, whereClause, sql);
        sql.append(whereClause);

        return getTransportations(locationCriteria, sql);
    }

    @Override
    public List<Transportation> getFlightsByCriteria(LocationCriteria locationCriteria) {
        StringBuilder sql = new StringBuilder("SELECT t FROM TransportationEntity t JOIN t.fromLocation tf ");
        StringBuilder whereClause = new StringBuilder("WHERE 1=1 ");

        if (CollectionUtils.isNotEmpty(locationCriteria.getDestinationIdList())) {
            sql.append("JOIN t.toLocation tt ");
            whereClause.append("AND tt.id IN :destinationIdList ");
        }
        if (CollectionUtils.isNotEmpty(locationCriteria.getOriginIdList())) {
            whereClause.append("AND tf.id IN :originIdList ");
        }
        if (locationCriteria.getRootOriginId() != null) {
            // We need to filter airports that have transportation by the root origin location.
            // rootOrigin > airport > airport > destination
            whereClause.append(" AND tf.id IN (SELECT t2.toLocation.id FROM TransportationEntity t2 WHERE t2.fromLocation.id = :rootOriginId) ");
        }

        whereClauseByCriteria(locationCriteria, whereClause, sql);
        sql.append(whereClause);

        return getTransportations(locationCriteria, sql);
    }


    private void whereClauseByCriteria(LocationCriteria locationCriteria, StringBuilder whereClause, StringBuilder sql) {
        if (locationCriteria.getExcludedTransportationType() != null) {
            whereClause.append("AND t.transportationType.name <> :excludedTransportationType ");
        }
        if (locationCriteria.getTransportationType() != null) {
            whereClause.append("AND t.transportationType.name = :transportationType ");
        }
        if (locationCriteria.getAvailableDay() != null) {
            //sql.append("JOIN t.availableDays ad ");
            whereClause.append("AND (:availableDay MEMBER OF t.availableDays) ");
        }
    }

    private List<Transportation> getTransportations(LocationCriteria locationCriteria, StringBuilder sql) {
        TypedQuery<TransportationEntity> query = entityManager
                .createQuery(sql.toString(), TransportationEntity.class);

        if (CollectionUtils.isNotEmpty(locationCriteria.getDestinationIdList())) {
            query.setParameter("destinationIdList", locationCriteria.getDestinationIdList());
        }
        if (CollectionUtils.isNotEmpty(locationCriteria.getOriginIdList())) {
            query.setParameter("originIdList", locationCriteria.getOriginIdList());
        }
        if (locationCriteria.getExcludedTransportationType() != null) {
            query.setParameter("excludedTransportationType", locationCriteria.getExcludedTransportationType());
        }
        if (locationCriteria.getTransportationType() != null) {
            query.setParameter("transportationType", locationCriteria.getTransportationType());
        }
        if (locationCriteria.getAvailableDay() != null) {
            query.setParameter("availableDay", locationCriteria.getAvailableDay());
        }
        if (locationCriteria.getRootOriginId() != null) {
            query.setParameter("rootOriginId", locationCriteria.getRootOriginId());
        }

        return query.getResultList()
                .stream()
                .map(transportationEntityMapper::toTransportation)
                .toList();
    }

    @Override
    public List<Transportation> search(String search, int page, int pageSize) {
        search = "%" + search + "%";
        return transportationEntityRepository.findTransportationEntitiesByFromLocation_NameLikeOrToLocation_NameLike(
                search, search, PageRequest.of(page, pageSize)
        ).stream().map(transportationEntityMapper::toTransportation).toList();
    }

    @Override
    public List<Transportation> page(int page, int size) {
        return transportationEntityRepository.findAll(PageRequest.of(page, size))
                .stream().map(transportationEntityMapper::toTransportation).toList();
    }

    @Override
    public Transportation findById(UUID uuid) {
        return transportationEntityRepository.findById(uuid)
                .map(transportationEntityMapper::toTransportation)
                .orElse(null);
    }

    @Override
    public void save(Transportation entity) {
        var transportationEntity = transportationEntityMapper.toTransportationEntity(entity);
        transportationEntityRepository.save(transportationEntity);
        entity.setId(transportationEntity.getId());
    }

    @Override
    public void deleteById(UUID id) {
        transportationEntityRepository.deleteById(id);
    }

    @Override
    public void update(Transportation entity) {
        var transportationEntity = transportationEntityMapper.toTransportationEntityForUpdate(entity);

        transportationEntityRepository.findById(transportationEntity.getId())
                .ifPresent(existingEntity -> {
                    existingEntity.setTransportationType(transportationEntity.getTransportationType());
                    existingEntity.setAvailableDays(transportationEntity.getAvailableDays());
                    transportationEntityRepository.save(existingEntity);
                });
    }

    @Override
    public long count(String search) {
        if (search == null || search.isEmpty()) {
            return transportationEntityRepository.count();
        }
        search = "%" + search + "%";
        return transportationEntityRepository.countByFromLocation_NameLikeOrToLocation_NameLike(search, search);
    }

    @Override
    public TransportationType getTransportationTypeById(Long id) {
        return transportationEntityMapper.toTransportationType(
                transportationTypeEntityRepository.findById(id).orElse(null)
        );
    }
}
