package org.example.routeplaner.infrastructure.persistence.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.routeplaner.application.dto.response.location.CityResponse;
import org.example.routeplaner.application.dto.response.location.CountryResponse;
import org.example.routeplaner.application.dto.response.location.SearchableLocationResponse;
import org.example.routeplaner.application.ports.output.LocationApplicationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class LocationApplicationRepositoryImpl implements LocationApplicationRepository {

    @PersistenceContext
    private final EntityManager em;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<SearchableLocationResponse> getAllSearchableLocations() {
        String query = "SELECT new org.example.routeplaner.application.dto.response.location.SearchableLocationResponse(l.id, l.name) " +
                       "FROM LocationEntity l";
        return em.createQuery(query, SearchableLocationResponse.class)
                 .getResultList();
    }

    @Override
    public List<CountryResponse> getAllCountries() {
        // dynamic instantiation in a sub-query is unsupported

        Map<Long, CountryResponse> countryResponseMap = new HashMap<>();
        jdbcTemplate.query("select c.country_id, c.name, ci.city_id, ci.name as ci_name " +
                        "from countries c left join cities ci on c.country_id = ci.country_id ",
                (rs, rowNum) -> {
                    Long countryId = rs.getLong("country_id");
                    String name = rs.getString("name");

                    CountryResponse countryResponse = countryResponseMap.computeIfAbsent(
                            countryId,
                            id -> new CountryResponse(id, name, new ArrayList<>())
                    );

                    countryResponse.getCities().add(
                            new CityResponse(rs.getLong("city_id"), rs.getString("ci_name"))
                    );

                    return null;
                });

        return new ArrayList<>(countryResponseMap.values());
    }





}
