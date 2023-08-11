package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.dto.PigeonLabelDto;
import tech.filatov.pigeoner.dto.SectionDto;
import tech.filatov.pigeoner.model.dovecote.Section;
import tech.filatov.pigeoner.repository.PigeonRepository;
import tech.filatov.pigeoner.repository.SectionRepository;
import tech.filatov.pigeoner.util.CommonUtil;
import tech.filatov.pigeoner.util.SectionUtil;
import tech.filatov.pigeoner.util.exception.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final SectionRepository repository;
    private final PigeonRepository pigeonRepository;

    public SectionService(SectionRepository repository, PigeonRepository pigeonRepository) {
        this.repository = repository;
        this.pigeonRepository = pigeonRepository;
    }

    public Section get(long id, long userId) {
        return repository.findByIdAndOwnerId(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public List<SectionDto> getAllDto(long userId) {
        return repository.getAll(userId);
    }

    public List<SectionDto> getAllWithFullAddress(long userId) {
        List<SectionDto> sections = getAllDto(userId);
        return SectionUtil.fillAddressData(sections);
    }

    public SectionDto getDtoWithFullAddress(long id, long userId) {
        Map<Long, SectionDto> sectionsMap = CommonUtil.getLookupMapFrom(getAllWithFullAddress(userId));
        return sectionsMap.get(id);
    }

    public List<SectionDto> getDtoHierarchicalStructure() {
        List<SectionDto> sectionWithoutHierarchy = repository.getAllWithoutNests();
        return SectionUtil.makeHierarchy(sectionWithoutHierarchy);
    }

    public List<SectionDto> getSectionsDtoTreeWithPigeons(long userId) {
        List<SectionDto> sectionsWithoutHierarchy = repository.getAllWithInfo();
        List<PigeonLabelDto> pigeons = pigeonRepository.getAllLabelDto(userId);
        insertPigeonsToSections(pigeons, sectionsWithoutHierarchy);
        return SectionUtil.makeHierarchy(sectionsWithoutHierarchy);
    }

    private void insertPigeonsToSections(List<PigeonLabelDto> pigeons, List<SectionDto> sections) {
        Map<Long, SectionDto> sectionsMap = sections.stream()
                .collect(Collectors.toMap(SectionDto::getId, Function.identity()));
        for (PigeonLabelDto pigeon : pigeons) {
            sectionsMap.get(pigeon.getSectionId()).getPigeons().add(pigeon);
        }
    }
}
