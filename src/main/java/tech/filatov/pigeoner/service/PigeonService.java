package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import tech.filatov.pigeoner.dto.*;
import tech.filatov.pigeoner.model.Keeper;
import tech.filatov.pigeoner.model.User;
import tech.filatov.pigeoner.model.dovecote.Section;
import tech.filatov.pigeoner.model.dovecote.SectionType;
import tech.filatov.pigeoner.model.pigeon.Pigeon;
import tech.filatov.pigeoner.repository.flight.FlightResultRepository;
import tech.filatov.pigeoner.repository.pigeon.PigeonRepository;
import tech.filatov.pigeoner.util.CommonUtil;
import tech.filatov.pigeoner.util.PigeonUtil;
import tech.filatov.pigeoner.util.exception.NotFoundException;
import tech.filatov.pigeoner.util.exception.NotPassValidationException;
import tech.filatov.pigeoner.validator.PigeonValidator;

import java.util.List;
import java.util.Map;

import static tech.filatov.pigeoner.util.PigeonUtil.fillWithUpdatedFields;
import static tech.filatov.pigeoner.util.PigeonUtil.getPigeonFrom;
import static tech.filatov.pigeoner.util.ValidationUtil.checkNotFoundWithId;

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

    public Pigeon findOne(long id, long userId) {
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
        return repository.findOneDto(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
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

    public PigeonDto create(PigeonShallowDto pigeonShallowDto, long userId) {
        Pigeon pigeon = getPigeonFrom(pigeonShallowDto);
        initializeFullStateFrom(pigeonShallowDto, pigeon, userId);

        pigeon = save(pigeon);
        //noinspection ConstantConditions
        return getPigeonDto(pigeon.getId(), userId);
    }

    public PigeonDto update(PigeonShallowDto pigeonShallowDto, long id, long userId) {
        Pigeon pigeon = fillWithUpdatedFields(findOne(id, userId), pigeonShallowDto);
        initializeFullStateFrom(pigeonShallowDto, pigeon, userId);

        pigeon = save(pigeon);
        //noinspection ConstantConditions
        return getPigeonDto(pigeon.getId(), userId);
    }

    @Transactional
    protected Pigeon save(Pigeon pigeon) {
        validate(pigeon);
        return repository.save(pigeon);
    }

    @Transactional
    public void delete(long id, long userId) {
        checkNotFoundWithId(repository.deleteByIdAndOwnerId(id, userId) != 0, id);
    }

    private void validate(Pigeon pigeon) {
        Errors errors = new BeanPropertyBindingResult(pigeon, "pigeon");
        validator.validate(pigeon, errors);
        if (errors.hasErrors()) {
            throw new NotPassValidationException(errors);
        }
    }

    private void initializeFullStateFrom(PigeonShallowDto source, Pigeon pigeon, long userId) {
        pigeon.setOwner(userService.get(userId));
        if (source.getMateId() != null) {
            pigeon.setMate(findOne(source.getMateId(), userId));
        }
        if (source.getFatherId() != null) {
            pigeon.setFather(findOne(source.getFatherId(), userId));
        }
        if (source.getMotherId() != null) {
            pigeon.setMother(findOne(source.getMotherId(), userId));
        }
        if (source.getSectionId() != null) {
            Section section = sectionService.findOne(source.getSectionId(), userId);
            if (section.getType() == SectionType.NEST) {
                section = sectionService.getWithPigeons(source.getSectionId(), userId);
            }
            pigeon.setSection(section);
        }
        if (source.getKeeperId() != null) {
            pigeon.setKeeper(keeperService.get(source.getKeeperId(), userId));
        }
        if (source.getColor() != null) {
            pigeon.setColor(colorService.getColorByName(source.getColor(), userId));
        }
        User owner = pigeon.getOwner() != null ? pigeon.getOwner() : null;
        Keeper mainKeeper = owner != null ? owner.getKeeper() : null;
        Keeper pigeonKeeper = pigeon.getKeeper();
        if (pigeonKeeper != null && pigeonKeeper.equals(mainKeeper)) {
            pigeon.setOwn(true);
        }
    }
}
