package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.dto.FlightDto;
import tech.filatov.pigeoner.repository.flight.FlightRepository;

import java.util.List;

@Service
public class FlightService {
    private final FlightRepository repository;

    public FlightService(FlightRepository repository) {
        this.repository = repository;
    }

    public List<FlightDto> getAllFlightDto(long userId) {
        return repository.getAllFlightDto(userId);
    }

    public FlightDto getFlightDto(long id, long userId) {
        return repository.getFlightDtoById(id, userId);
    }
}
