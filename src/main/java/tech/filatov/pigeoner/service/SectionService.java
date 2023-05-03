package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.dto.SectionHierarchicalDto;
import tech.filatov.pigeoner.repository.SectionRepository;
import tech.filatov.pigeoner.util.SectionUtil;

import java.util.List;

@Service
public class SectionService {

    private final SectionRepository repository;

    public SectionService(SectionRepository repository) {
        this.repository = repository;
    }


    public List<SectionHierarchicalDto> getHierarchicalStructure() {
        List<SectionHierarchicalDto> sectionWithoutHierarchy = repository.getAllHierarchical();
        return SectionUtil.makeHierarchy(sectionWithoutHierarchy);
    }
}
