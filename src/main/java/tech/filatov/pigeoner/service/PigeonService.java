package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.dto.FlightResultDto;
import tech.filatov.pigeoner.dto.PigeonTableDto;
import tech.filatov.pigeoner.dto.PigeonWithAncestorsDto;
import tech.filatov.pigeoner.repository.FlightResultRepository;
import tech.filatov.pigeoner.repository.PigeonRepository;
import tech.filatov.pigeoner.util.PigeonUtil;

import java.util.List;
import java.util.Map;

import static tech.filatov.pigeoner.util.PigeonUtil.*;

@Service
public class PigeonService {

    private final PigeonRepository repository;
    private final FlightResultRepository flightResultRepository;

    public PigeonService(PigeonRepository repository, FlightResultRepository flightResultRepository) {
        this.repository = repository;
        this.flightResultRepository = flightResultRepository;
    }

    public List<PigeonTableDto> getAll()  {
        return getDtos(repository.getAll());
    }

    public List<PigeonTableDto> getAll(Map<String, String> filterParameters) {
        return getDtos(repository.getFiltered(filterParameters));
    }

    public PigeonWithAncestorsDto getWithAncestorsAndFlights(int id, long userId) {
        List<PigeonWithAncestorsDto> pigeons = repository.getWithAncestorsById(id);
        PigeonWithAncestorsDto pigeon = PigeonUtil.buildPedigree(pigeons);

        List<FlightResultDto> flights = flightResultRepository.getAllByPigeonId(id, userId);
        pigeon.setFlights(flights);

        return pigeon;
    }
}
