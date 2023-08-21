package tech.filatov.pigeoner.repository.flight;

import java.util.Map;

public interface FlightResultRepositoryCustom {
    Map<Long, Long> getNumberOfMyParticipantsForEveryFlight(long userId);
}
