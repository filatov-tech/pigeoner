package tech.filatov.pigeoner.util;

import org.springframework.stereotype.Component;
import tech.filatov.pigeoner.dto.FlightResultDto;
import tech.filatov.pigeoner.model.flight.AfterFlightCondition;
import tech.filatov.pigeoner.model.flight.FlightResult;

@Component
public class FlightResultMapper {
    public FlightResultMapper() {}

    public FlightResult instantiateFrom(FlightResultDto dto) {
        FlightResult flightResult = new FlightResult();
        return setDataTo(flightResult, dto);
    }

    public FlightResult fillInstantiatedWith(FlightResultDto source, FlightResult target) {
        return setDataTo(target, source);
    }

    private FlightResult setDataTo(FlightResult target, FlightResultDto source) {
        target.setArrivalTime(source.getArrivalTime());
        target.setCondition(source.getAfterFlightCondition() == null
                ? AfterFlightCondition.NORMAL
                : AfterFlightCondition.valueOf(source.getAfterFlightCondition())
        );
        return target;
    }
}
