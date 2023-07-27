package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.dto.FilterParams;
import tech.filatov.pigeoner.dto.FlightResultDto;
import tech.filatov.pigeoner.dto.PigeonTableDto;
import tech.filatov.pigeoner.dto.PigeonWithAncestorsDto;
import tech.filatov.pigeoner.repository.FlightResultRepository;
import tech.filatov.pigeoner.repository.PigeonRepository;
import tech.filatov.pigeoner.util.PigeonUtil;

import java.util.List;

@Service
public class PigeonService {

    private final PigeonRepository repository;
    private final FlightResultRepository flightResultRepository;

    public PigeonService(PigeonRepository repository, FlightResultRepository flightResultRepository) {
        this.repository = repository;
        this.flightResultRepository = flightResultRepository;
    }

    public List<PigeonTableDto> getAll(long userId)  {
        return repository.getFiltered(null, userId);
    }

    public List<PigeonTableDto> getAll(FilterParams params, long userId) {
        return repository.getFiltered(params, userId);
    }

    public PigeonWithAncestorsDto getWithAncestorsAndFlights(int id, long userId) {
        List<PigeonWithAncestorsDto> pigeons = repository.getWithAncestorsById(id);
        PigeonWithAncestorsDto pigeon = PigeonUtil.buildPedigree(pigeons);

        List<FlightResultDto> flights = flightResultRepository.getAllByPigeonId(id, userId);
        pigeon.setFlights(flights);

        return pigeon;
    }
}
