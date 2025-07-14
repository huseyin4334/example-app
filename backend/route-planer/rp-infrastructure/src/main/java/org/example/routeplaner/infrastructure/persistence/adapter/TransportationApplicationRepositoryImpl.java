package org.example.routeplaner.infrastructure.persistence.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.routeplaner.application.dto.response.transportation.TransportationTypeResponse;
import org.example.routeplaner.application.ports.output.TransformationApplicationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TransportationApplicationRepositoryImpl implements TransformationApplicationRepository {

    @PersistenceContext
    private final EntityManager em;


    @Override
    public List<TransportationTypeResponse> getTransformationTypes() {
        String query = "SELECT new org.example.routeplaner.application.dto.response.transportation.TransportationTypeResponse(t.id, t.name) " +
                       "FROM TransportationTypeEntity t";
        return em.createQuery(query, TransportationTypeResponse.class)
                 .getResultList();
    }
}
