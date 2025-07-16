package org.example.routeplaner.domain.model.aggregate;

import lombok.Getter;
import org.example.common.TransportConstants;
import org.example.routeplaner.domain.exception.RouteDomainException;

@Getter
public class Route {
    private Transportation beforeFlight;
    private Transportation flight;
    private Transportation afterFlight;

    public void setBeforeFlight(Transportation beforeFlight) {
        if (beforeFlight.getTransportationType().getName().equals(TransportConstants.FLIGHT))
            throw new RouteDomainException("Before flight transportation cannot be of type FLIGHT");
        this.beforeFlight = beforeFlight;
    }

    public void setFlight(Transportation flight) {
        if (this.beforeFlight == null)
            throw new RouteDomainException("Before flight transportation must be set before flight");
        if (!flight.getTransportationType().getName().equals(TransportConstants.FLIGHT))
            throw new RouteDomainException("Flight transportation must be of type FLIGHT");
        this.flight = flight;
    }

    public void setAfterFlight(Transportation afterFlight) {
        if (this.flight == null)
            throw new RouteDomainException("Flight transportation must be set before after flight");
        if (afterFlight.getTransportationType().getName().equals(TransportConstants.FLIGHT))
            throw new RouteDomainException("After flight transportation cannot be of type FLIGHT");
        this.afterFlight = afterFlight;
    }
}
