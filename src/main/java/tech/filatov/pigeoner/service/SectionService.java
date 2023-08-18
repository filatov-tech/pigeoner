package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.filatov.pigeoner.dto.PigeonLabelDto;
import tech.filatov.pigeoner.dto.SectionDto;
import tech.filatov.pigeoner.model.dovecote.Section;
import tech.filatov.pigeoner.repository.pigeon.PigeonRepository;
import tech.filatov.pigeoner.repository.SectionRepository;
import tech.filatov.pigeoner.util.CommonUtil;
import tech.filatov.pigeoner.util.ValidationUtil;
import tech.filatov.pigeoner.util.exception.NotFoundException;
import tech.filatov.pigeoner.validator.SectionValidator;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tech.filatov.pigeoner.util.SectionUtil.*;
import static tech.filatov.pigeoner.util.ValidationUtil.validate;

@Service
public class SectionService {

    private final SectionRepository repository;
    private final UserService userService;
    private final PigeonRepository pigeonRepository;
    private final SectionValidator validator;

    public SectionService(SectionRepository repository, UserService userService, PigeonRepository pigeonRepository, SectionValidator validator) {
        this.repository = repository;
        this.userService = userService;
        this.pigeonRepository = pigeonRepository;
        this.validator = validator;
    }

    public Section findOne(long id, long userId) {
        return repository.findByIdAndOwnerId(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public Section getWithPigeons(long id, long userId) {
        return repository.findWithPigeonsByIdAndOwnerId(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public List<SectionDto> getAllDto(long userId) {
        return repository.getAll(userId);
    }

    public List<SectionDto> getAllWithFullAddress(long userId) {
        List<SectionDto> sections = getAllDto(userId);
        return fillAddressData(sections);
    }

    public SectionDto getDtoWithFullAddress(long id, long userId) {
        Map<Long, SectionDto> sectionsMap = CommonUtil.getLookupMapFrom(getAllWithFullAddress(userId));
        return sectionsMap.get(id);
    }

    public List<SectionDto> getDtoHierarchicalStructure() {
        List<SectionDto> sectionWithoutHierarchy = repository.getAllWithoutNests();
        return makeHierarchy(sectionWithoutHierarchy);
    }

    public List<SectionDto> getSectionsDtoTreeWithPigeons(long userId) {
        List<SectionDto> sectionsWithoutHierarchy = repository.getAllWithInfo();
        List<PigeonLabelDto> pigeons = pigeonRepository.getAllLabelDto(userId);
        insertPigeonsToSections(pigeons, sectionsWithoutHierarchy);
        return makeHierarchy(sectionsWithoutHierarchy);
    }

    private void insertPigeonsToSections(List<PigeonLabelDto> pigeons, List<SectionDto> sections) {
        Map<Long, SectionDto> sectionsMap = sections.stream()
                .collect(Collectors.toMap(SectionDto::getId, Function.identity()));
        for (PigeonLabelDto pigeon : pigeons) {
            sectionsMap.get(pigeon.getSectionId()).getPigeons().add(pigeon);
        }
    }

    public SectionDto getDto(long id, long userId) {
        return repository.findDtoByIdAndOwnerId(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public SectionDto createOrUpdate(SectionDto dto, long userId) {
        Section section = instantiateFrom(dto, userId);
        validate(section, validator);
        return save(section, userId);
    }

    private Section instantiateFrom(SectionDto dto, long userId) {
        Section section;
        if (dto.isNew()) {
            section = makeInstanceFrom(dto);
            section.setOwner(userService.get(userId));
        } else {
            section = fillWithUpdatedFields(findOne(dto.getId(), userId), dto);
        }
        if (dto.getParentId() != null) {
            section.setParent(findOne(dto.getParentId(), userId));
        }
        return section;
    }

    @Transactional
    SectionDto save(Section section, long userId) {
        Section saved = repository.save(section);
        //noinspection ConstantConditions
        return getDto(saved.getId(), userId);
    }

    @Transactional
    public void delete(long id, long userId) {
        ValidationUtil.checkNotFoundWithId(repository.delete(id, userId) != 0, id);
    }
}
