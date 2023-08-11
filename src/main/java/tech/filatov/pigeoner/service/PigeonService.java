package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import tech.filatov.pigeoner.dto.*;
import tech.filatov.pigeoner.model.pigeon.Pigeon;
import tech.filatov.pigeoner.repository.FlightResultRepository;
import tech.filatov.pigeoner.repository.PigeonRepository;
import tech.filatov.pigeoner.util.CommonUtil;
import tech.filatov.pigeoner.util.PigeonUtil;
import tech.filatov.pigeoner.util.exception.NotFoundException;
import tech.filatov.pigeoner.util.exception.ValidationException;
import tech.filatov.pigeoner.validator.PigeonValidator;

import java.util.List;
import java.util.Map;

@Service
public class PigeonService {

    private final UserService userService;
    private final SectionService sectionService;
    private final PigeonRepository repository;
    private final ColorService colorService;
    private final PigeonValidator validator;
    private final KeeperService keeperService;
    private final FlightResultRepository flightResultRepository;

    public PigeonService(UserService userService, SectionService sectionService, PigeonRepository repository, ColorService colorService, PigeonValidator validator, KeeperService keeperService, FlightResultRepository flightResultRepository) {
        this.userService = userService;
        this.sectionService = sectionService;
        this.repository = repository;
        this.colorService = colorService;
        this.validator = validator;
        this.keeperService = keeperService;
        this.flightResultRepository = flightResultRepository;
    }

    public Pigeon get(long id, long userId) {
        return repository.findByIdAndOwnerId(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public List<PigeonShallowDto> getAll(long userId)  {
        List<PigeonShallowDto> pigeons = repository.getAllPigeonShallowDto(userId);
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

    public PigeonDto getPigeonDtoWithoutNestedDto(long id, long userId) {
        return repository.getPigeonDto(id, userId);
    }

    public PigeonDto getPigeonDto(long id, long userId) {
        PigeonDto pigeon = getPigeonDtoWithoutNestedDto(id, userId);
        return makeFull(pigeon, userId);
    }

    public PigeonDto getWithAncestors(long id, long userId) {
        PigeonDto pigeon = getPigeonDto(id, userId);

        List<PigeonDto> pigeons = repository.getWithAncestorsById(id, userId);
        PigeonDto pigeonWithAncestors = PigeonUtil.buildPedigree(pigeons, id);

        pigeon.setMother(pigeonWithAncestors.getMother());
        pigeon.setFather(pigeonWithAncestors.getFather());
        return pigeon;
    }

    private PigeonDto makeFull(PigeonDto pigeon, long userId) {
        if (pigeon.getSectionId() != null) {
            SectionDto section = sectionService.getDtoWithFullAddress(pigeon.getSectionId(), userId);
            pigeon.setSection(section);
        }
        if (pigeon.getMateId() != null) {
            pigeon.setMate(getPigeonDtoWithoutNestedDto(pigeon.getMateId(), userId));
        }
        if (pigeon.getFatherId() != null) {
            pigeon.setFather(getPigeonDtoWithoutNestedDto(pigeon.getFatherId(), userId));
        }
        if (pigeon.getMotherId() != null) {
            pigeon.setMother(getPigeonDtoWithoutNestedDto(pigeon.getMotherId(), userId));
        }
        pigeon.setColor(colorService.findColorByPigeonId(pigeon.getId(), userId).getName());
        List<FlightResultDto> flightResults = flightResultRepository.getAllByPigeonId(pigeon.getId(), userId);
        if (!flightResults.isEmpty()) {
            pigeon.setFlights(flightResults);
        }
        return pigeon;
    }

    @Transactional
    public PigeonDto create(PigeonShallowDto pigeonShallowDto, long userId) {
        Pigeon pigeon = PigeonUtil.getPigeonFrom(pigeonShallowDto);
        initializePigeonNestedObjectsByIdsFrom(pigeonShallowDto, pigeon, userId);

        Errors errors = new BeanPropertyBindingResult(pigeon, "pigeon");
        validator.validate(pigeon, errors);

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        pigeon = repository.save(pigeon);

        return getPigeonDto(pigeon.getId(), userId);
    }

    private void initializePigeonNestedObjectsByIdsFrom(PigeonShallowDto idsSource, Pigeon pigeon, long userId) {
        pigeon.setOwner(userService.get(userId));
        if (idsSource.getMateId() != null) {
            pigeon.setMate(get(idsSource.getMateId(), userId));
        }
        if (idsSource.getFatherId() != null) {
            pigeon.setFather(get(idsSource.getFatherId(), userId));
        }
        if (idsSource.getMotherId() != null) {
            pigeon.setMother(get(idsSource.getMotherId(), userId));
        }
        if (idsSource.getSectionId() != null) {
            pigeon.setSection(sectionService.get(idsSource.getSectionId(), userId));
        }
        if (idsSource.getKeeperId() != null) {
            pigeon.setKeeper(keeperService.get(idsSource.getKeeperId(), userId));
        }
        if (idsSource.getColor() != null) {
            pigeon.setColor(colorService.getColorByName(idsSource.getColor(), userId));
        }
    }
}
