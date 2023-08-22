package tech.filatov.pigeoner.util;

import org.springframework.stereotype.Component;
import tech.filatov.pigeoner.dto.FlightDto;
import tech.filatov.pigeoner.model.flight.Flight;
import tech.filatov.pigeoner.model.flight.FlightType;

@Component
public class FlightMapper {
    public FlightMapper() {}

    public Flight instantiateFrom(FlightDto dto) {
        Flight flight = new Flight();
        return setDataTo(flight, dto);
    }

    public Flight fillInstantiatedWith(FlightDto dto, Flight target) {
        return setDataTo(target, dto);
    }

    private Flight setDataTo(Flight target, FlightDto source) {
        target.setDeparture(source.getDeparture());
        target.setFlightType(FlightType.valueOf(source.getFlightType()));
        target.setTotalParticipants(source.getTotalParticipants());
        target.setPassingThreshold(source.getPassingThreshold());
        target.setMyPassed(source.getMyPassed());
        return target;
    }
}
