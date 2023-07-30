package tech.filatov.pigeoner.web.section;

import org.springframework.web.bind.annotation.*;
import tech.filatov.pigeoner.AuthorizedUser;
import tech.filatov.pigeoner.dto.PigeonTableDto;
import tech.filatov.pigeoner.dto.SectionDto;
import tech.filatov.pigeoner.repository.SectionRepository;
import tech.filatov.pigeoner.service.SectionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sections")
public class SectionRestController {

    private final AuthorizedUser authUser = new AuthorizedUser();
    private final SectionRepository repository;
    private final SectionService service;

    public SectionRestController(SectionRepository repository, SectionService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/{id}/pigeons")
    public List<PigeonTableDto> getSectionsPigeon(@PathVariable long id) {
        return service.getSectionsPigeons(id);
    }

    @GetMapping
    public List<SectionDto> getSectionsData() {
        return service.getSectionsTreeWithPigeons(authUser.getId());
    }

    @GetMapping("/info")
    public List<SectionDto> getTopLevelSectionInfo() {
        return repository.getTopLevelCommonSectionInfo();
    }

    @GetMapping("/info/{id}")
    public List<SectionDto> getSectionInfo(@PathVariable int id) {
        return repository.getCommonSectionInfoById(id);
    }

    @GetMapping("/hierarchy")
    public List<SectionDto> getHierarchicalStructure() {
        return service.getHierarchicalStructure();
    }
}
