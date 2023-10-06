package tech.filatov.pigeoner.repository.flight;

import tech.filatov.pigeoner.model.flight.FlightResult;

import java.util.List;
import java.util.Map;

public interface FlightResultRepositoryCustom {
    Map<Long, Integer> getNumberOfMyParticipantsForEveryFlight(long userId);

    List<FlightResult> findAllByFlightIdAndKeeperId(long flightId, long keeperId, long userId);

    List<FlightResult> getAllByLaunchPoint(long launchPointId, long userId);
}
