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

    public List<PigeonShallowDto> getAll(long userId)  {
        List<PigeonShallowDto> pigeons = repository.getAll(userId);
        return addSectionsTo(pigeons, userId);
    }

    public List<PigeonShallowDto> getAllFiltered(FilterParams params, long userId) {
        List<PigeonShallowDto> pigeons = repository.getFiltered(params, userId);
        return addSectionsTo(pigeons, userId);
    }

    private List<PigeonShallowDto> addSectionsTo(List<PigeonShallowDto> pigeons, long userId) {
        Map<Long, SectionDto> sections = CommonUtil.getLookupMapFrom(
                sectionService.getAllWithFullAddress(userId)
        );
        for (PigeonShallowDto pigeon : pigeons) {
            if (pigeon.getSectionId() == null) continue;
            pigeon.setSection(sections.get(pigeon.getSectionId()));
        }
        return pigeons;
    }

    public PigeonDto getWithAncestorsAndFlights(long id, long userId) {
        List<PigeonDto> pigeons = repository.getWithAncestorsById(id, userId);
        PigeonDto pigeon = PigeonUtil.buildPedigree(pigeons, id);

        List<FlightResultDto> flights = flightResultRepository.getAllByPigeonId(id, userId);
        pigeon.setFlights(flights);

        return pigeon;
    }
}
