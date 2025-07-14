package org.example.routeplaner.userinterface.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import org.example.common.application.BaseListResponse;
import org.example.routeplaner.application.dto.command.DeleteCommand;
import org.example.routeplaner.application.dto.command.GetQuery;
import org.example.routeplaner.application.dto.command.SearchQuery;
import org.example.routeplaner.application.dto.command.location.LocationCreateCommand;
import org.example.routeplaner.application.dto.response.location.LocationResponse;
import org.example.routeplaner.application.ports.input.LocationApplicationService;
import org.example.routeplaner.domain.exception.RouteDomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
@SuppressWarnings("unchecked")
class LocationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    LocationApplicationService locationApplicationService;

    @Autowired
    ObjectMapper objectMapper;

    LocationCreateCommand validCommand;

    @BeforeEach
    void setUp() {
        validCommand = new LocationCreateCommand(
                UUID.randomUUID().toString(),
                "TestName",
                1L,
                "CODE"
        );
    }

    @Test
    void testCreateLocation() throws Exception {
        mockMvc.perform(put("/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validCommand)))
                .andExpect(status().isOk());
        verify(locationApplicationService).create(any(LocationCreateCommand.class));
    }

    @Test
    void testUpdateLocation() throws Exception {
        mockMvc.perform(post("/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validCommand)))
                .andExpect(status().isOk());
        verify(locationApplicationService).update(any(LocationCreateCommand.class));
    }

    @Test
    void testDeleteLocation() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/locations/{id}", id))
                .andExpect(status().isOk());
        verify(locationApplicationService).delete(any(DeleteCommand.class));
    }

    @Test
    void testGetLocation() throws Exception {
        UUID id = UUID.randomUUID();
        LocationResponse response = new LocationResponse();
        response.setId(id);
        when(locationApplicationService.findById(any(GetQuery.class))).thenReturn(response);

        mockMvc.perform(get("/locations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void testSearchLocation() throws Exception {
        SearchQuery query = new SearchQuery("test", 0, 10);
        BaseListResponse<LocationResponse> resp = new BaseListResponse<>();
        LocationResponse loc = new LocationResponse();
        loc.setId(UUID.randomUUID());
        resp.setItems(List.of(loc));
        resp.setTotal(1);
        resp.setPageSize(0);
        when(locationApplicationService.list(any(SearchQuery.class))).thenReturn(resp);

        mockMvc.perform(post("/locations/page")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)));
    }

    @Test
    void testCreateLocationValidationError() throws Exception {
        LocationCreateCommand invalidCommand = new LocationCreateCommand(
                "", "", 0L, ""
        );
        mockMvc.perform(put("/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]").isNotEmpty());
    }

    @Test
    void testUpdateLocationThrowsDomainException() throws Exception {
        doThrow(new RouteDomainException("domain error")).when(locationApplicationService).update(any());
        mockMvc.perform(post("/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validCommand)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message[0]").value("domain error"));
    }

    @Test
    void testDeleteLocationThrowsValidationException() throws Exception {
        doThrow(new ValidationException("validation error")).when(locationApplicationService).delete(any());
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/locations/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]").value("validation error"));
    }

    @Test
    void testGetLocationThrowsGenericException() throws Exception {
        doThrow(new RuntimeException("generic error")).when(locationApplicationService).findById(any());
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/locations/{id}", id))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message[0]").value("generic error"));
    }
}
