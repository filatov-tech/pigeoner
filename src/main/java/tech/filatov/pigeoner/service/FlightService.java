package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.dto.FlightTableDto;
import tech.filatov.pigeoner.repository.FlightRepository;

import java.util.List;

@Service
public class FlightService {
    private final FlightRepository repository;

    public FlightService(FlightRepository repository) {
        this.repository = repository;
    }


    public List<FlightTableDto> getAllWithNumberOfMyPigeons(long id) {
        return repository.getAllFlightTableDto(id);
    }
}
