package org.example.routeplaner.application.adapters;

import lombok.RequiredArgsConstructor;
import org.example.common.application.BaseListResponse;
import org.example.routeplaner.application.dto.command.DeleteCommand;
import org.example.routeplaner.application.dto.command.GetQuery;
import org.example.routeplaner.application.dto.command.SearchQuery;
import org.example.routeplaner.application.dto.command.transport.TransportationCreateCommand;
import org.example.routeplaner.application.dto.command.transport.TransportationUpdateCommand;
import org.example.routeplaner.application.dto.response.transportation.TransportationResponse;
import org.example.routeplaner.application.dto.response.transportation.TransportationTypeResponse;
import org.example.routeplaner.application.mapper.TransportationMapper;
import org.example.routeplaner.application.ports.input.TransportationApplicationService;
import org.example.routeplaner.application.ports.output.TransformationApplicationRepository;
import org.example.routeplaner.domain.model.aggregate.Transportation;
import org.example.routeplaner.domain.ports.output.repository.TransportationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TransportationApplicationServiceImpl implements TransportationApplicationService {
    private final TransportationMapper transportationMapper;
    private final TransportationRepository transportationRepository;
    private final TransformationApplicationRepository transportationApplicationRepository;

    @Transactional
    @Override
    public void create(TransportationCreateCommand command) {
        Transportation transportation = transportationMapper.toDomain(command);
        transportation.isValid();
        transportationRepository.save(transportation);
    }

    @Transactional
    @Override
    public void update(TransportationUpdateCommand command) {
        if (command.getId() == null)
            throw new IllegalArgumentException("Transportation ID must not be null for update.");

        Transportation transportation = transportationMapper.toDomain(command);
        //transportation.isValid();

        transportationRepository.update(transportation);
    }

    @Override
    public BaseListResponse<TransportationResponse> list(SearchQuery command) {
        if (command == null) {
            throw new IllegalArgumentException("SearchQuery command must not be null.");
        }

        BaseListResponse<TransportationResponse> response = new BaseListResponse<>();

        long count = transportationRepository.count(command.getSearchTerm());

        response.setPage(command.getPageNo());
        response.setPageSize(command.getPageSize());
        response.setTotal(count);

        if (count == 0 || ((long) command.getPageSize() * command.getPageNo()) > count) {
            return response;
        }

        List<Transportation> transportations;
        if (command.getSearchTerm() == null || command.getSearchTerm().isEmpty())
            transportations = transportationRepository.page(command.getPageNo(), command.getPageSize());
        else
            transportations = transportationRepository.search(command.getSearchTerm(), command.getPageNo(), command.getPageSize());

        response.setItems(
                transportations
                        .stream()
                        .map(transportationMapper::toTransportResponse)
                        .toList()
        );

        return response;
    }

    @Override
    public TransportationResponse findById(GetQuery<UUID> command) {
        if (command == null || command.getId() == null) {
            throw new IllegalArgumentException("GetQuery and its ID must not be null.");
        }
        return transportationMapper.toTransportResponse(transportationRepository.findById(command.getId()));
    }

    @Override
    public void delete(DeleteCommand<UUID> command) {
        if (command == null || command.getId() == null) {
            throw new IllegalArgumentException("DeleteCommand and its ID must not be null.");
        }
        transportationRepository.deleteById(command.getId());
    }

    @Override
    public BaseListResponse<TransportationTypeResponse> getAvailableTransportationTypes() {
        List<TransportationTypeResponse> types = transportationApplicationRepository.getTransformationTypes();
        BaseListResponse<TransportationTypeResponse> response = new BaseListResponse<>();
        response.setItems(types);
        response.setTotal(types.size());
        return response;
    }
}
