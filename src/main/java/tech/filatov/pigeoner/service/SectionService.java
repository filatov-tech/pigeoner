package tech.filatov.pigeoner.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.filatov.pigeoner.dto.PigeonLabelDto;
import tech.filatov.pigeoner.dto.SectionDto;
import tech.filatov.pigeoner.model.dovecote.Section;
import tech.filatov.pigeoner.model.dovecote.SectionType;
import tech.filatov.pigeoner.model.pigeon.Pigeon;
import tech.filatov.pigeoner.repository.SectionRepository;
import tech.filatov.pigeoner.util.CommonUtil;
import tech.filatov.pigeoner.util.ValidationUtil;
import tech.filatov.pigeoner.util.exception.NotFoundException;
import tech.filatov.pigeoner.validator.SectionValidator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tech.filatov.pigeoner.util.SectionUtil.*;
import static tech.filatov.pigeoner.util.ValidationUtil.validate;

@Service
public class SectionService {

    private final SectionRepository repository;
    private final UserService userService;
    private final PigeonService pigeonService;
    private final SectionValidator validator;

    public SectionService(SectionRepository repository, UserService userService, @Lazy PigeonService pigeonService, SectionValidator validator) {
        this.repository = repository;
        this.userService = userService;
        this.pigeonService = pigeonService;
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

    public List<SectionDto> getAllHierarchicalWithoutNests(long userId) {
        List<SectionDto> sectionWithoutHierarchy = repository.getAllWithoutNests(userId);
        return makeHierarchy(sectionWithoutHierarchy);
    }

    public List<SectionDto> getAllHierarchical(long userId) {
        List<SectionDto> sectionDtos = fillAddressData(repository.getAll(userId));
        return makeHierarchy(sectionDtos);
    }

    public List<SectionDto> getSectionsDtoTreeWithPigeons(long userId) {
        List<SectionDto> sectionsWithoutHierarchy = repository.getAllWithInfo(userId);
        List<PigeonLabelDto> pigeons = pigeonService.getAllLabelDto(userId);
        insertPigeonsToSections(pigeons, sectionsWithoutHierarchy);
        return makeHierarchy(sectionsWithoutHierarchy);
    }

    private void insertPigeonsToSections(List<PigeonLabelDto> pigeons, List<SectionDto> sections) {
        Map<Long, SectionDto> sectionsMap = sections.stream()
                .collect(Collectors.toMap(SectionDto::getId, Function.identity()));

        SectionDto inboxDovecote = SectionDto.getInboxForPigeonsWithoutSection();
        int inboxPigeonsCounter = 0;

        for (PigeonLabelDto pigeon : pigeons) {
            Long pigeonSectionId = pigeon.getSectionId();
            if (pigeonSectionId == null) {
                inboxPigeonsCounter++;
                inboxDovecote.getPigeons().add(pigeon);
            } else {
                sectionsMap.get(pigeonSectionId).getPigeons().add(pigeon);
            }
        }
        inboxDovecote.setPigeonsNumber(inboxPigeonsCounter);
        sections.add(inboxDovecote);
    }

    public SectionDto getDto(long id, long userId) {
        return repository.findDtoByIdAndOwnerId(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    @Transactional
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

    SectionDto save(Section section, long userId) {
        Section saved = repository.save(section);
        long savedId = Objects.requireNonNull(saved.getId());
        SectionDto dto = getDto(savedId, userId);
        dto.setPigeons(pigeonService.getLabelDtoBySectionId(savedId, userId));
        return dto;
    }

    @Transactional
    public void delete(long id, long userId) {
        Section sectionToDelete = repository.findWithParentByIdAndOwnerId(id, userId).orElse(null);
        ValidationUtil.checkNotFoundWithId(sectionToDelete != null, id);
        boolean isNestDeleting = sectionToDelete.getType() == SectionType.NEST;

        List<Pigeon> allPigeonToMove;
        List<Long> sectionsIds = repository.getIdListOfAllDescendantsById(id, userId);
        if (isNestDeleting) {
            allPigeonToMove = pigeonService.getAllBySectionId(sectionToDelete.getId(), userId);
        } else {
            sectionsIds.add(sectionToDelete.getId());
            allPigeonToMove = pigeonService.getAllBySectionsIds(sectionsIds, userId);
        }
        allPigeonToMove = changeParentSectionTo(sectionToDelete.getParent(), allPigeonToMove);
        pigeonService.saveAll(allPigeonToMove);

        repository.delete(sectionToDelete);
    }

    private List<Pigeon> changeParentSectionTo(@Nullable Section newParentSection, List<Pigeon> pigeons) {
        return pigeons.stream()
                .peek(pigeon -> pigeon.setSection(newParentSection))
                .collect(Collectors.toList());
    }
}
