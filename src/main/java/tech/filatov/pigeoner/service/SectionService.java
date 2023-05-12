package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.dto.PigeonLabelDto;
import tech.filatov.pigeoner.dto.PigeonTableDto;
import tech.filatov.pigeoner.dto.SectionDto;
import tech.filatov.pigeoner.dto.SectionHierarchicalDto;
import tech.filatov.pigeoner.repository.PigeonRepository;
import tech.filatov.pigeoner.repository.SectionRepository;
import tech.filatov.pigeoner.util.PigeonUtil;
import tech.filatov.pigeoner.util.SectionUtil;

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

    public List<SectionHierarchicalDto> getHierarchicalStructure() {
        List<SectionHierarchicalDto> sectionWithoutHierarchy = repository.getAllHierarchical();
        return SectionUtil.makeHierarchy(sectionWithoutHierarchy);
    }

    public List<PigeonTableDto> getSectionsPigeons(long id) {
        return PigeonUtil.getDtos(pigeonRepository.getAllByLocation(id));
    }

    public List<SectionDto> getSectionsTreeWithPigeons() {
        List<SectionDto> sectionsWithoutHierarchy = repository.getAllWithInfo();
        List<PigeonLabelDto> pigeons = pigeonRepository.getAllLabelDto();
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
