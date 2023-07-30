package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.dto.*;
import tech.filatov.pigeoner.repository.FlightResultRepository;
import tech.filatov.pigeoner.repository.PigeonRepository;
import tech.filatov.pigeoner.util.CommonUtil;
import tech.filatov.pigeoner.util.PigeonUtil;

import java.util.List;
import java.util.Map;

@Service
public class PigeonService {

    private final SectionService sectionService;
    private final PigeonRepository repository;
    private final FlightResultRepository flightResultRepository;

    public PigeonService(SectionService sectionService, PigeonRepository repository, FlightResultRepository flightResultRepository) {
        this.sectionService = sectionService;
        this.repository = repository;
        this.flightResultRepository = flightResultRepository;
    }

    public List<PigeonTableDto> getAll(long userId)  {
        return getAll(null, userId);
    }

    public List<PigeonTableDto> getAll(FilterParams params, long userId) {
        List<PigeonTableDto> pigeons = repository.getFiltered(params, userId);
        return addSectionsTo(pigeons, userId);
    }

    private List<PigeonTableDto> addSectionsTo(List<PigeonTableDto> pigeons, long userId) {
        Map<Long, SectionDto> sections = CommonUtil.getLookupMapFrom(
                sectionService.getAllWithFullAddress(userId)
        );
        for (PigeonTableDto pigeon : pigeons) {
            if (pigeon.getSectionId() == null) continue;
            pigeon.setSection(sections.get(pigeon.getSectionId()));
        }
        return pigeons;
    }

    public PigeonWithAncestorsDto getWithAncestorsAndFlights(int id, long userId) {
        List<PigeonWithAncestorsDto> pigeons = repository.getWithAncestorsById(id);
        PigeonWithAncestorsDto pigeon = PigeonUtil.buildPedigree(pigeons);

        List<FlightResultDto> flights = flightResultRepository.getAllByPigeonId(id, userId);
        pigeon.setFlights(flights);

        return pigeon;
    }
}
