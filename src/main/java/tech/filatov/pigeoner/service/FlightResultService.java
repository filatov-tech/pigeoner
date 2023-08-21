package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.dto.FlightResultDto;
import tech.filatov.pigeoner.repository.flight.FlightResultRepository;

import java.util.List;
import java.util.Map;

@Service
public class FlightResultService {
    private final FlightResultRepository repository;

    public FlightResultService(FlightResultRepository repository) {
        this.repository = repository;
    }

    public List<FlightResultDto> getAllByPigeonId(long id, long userId) {
        return repository.getAllByPigeonId(id, userId);
    }

    public List<FlightResultDto> getAllByFlightId(long id, long userId) {
        return repository.getAllByFlightId(id, userId);
    }

    public Map<Long, Long> getNumberOfMyParticipantsForEveryFlight(long userId) {
        return repository.getNumberOfMyParticipantsForEveryFlight(userId);
    }

    public long getNumberOfMyParticipantsByFlightId(long id, long userId) {
        return repository.getNumberOfMyParticipantsByFlightId(id, userId);
    }
}
