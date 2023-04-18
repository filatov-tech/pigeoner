package tech.filatov.pigeoner.web.section;

import org.springframework.web.bind.annotation.*;
import tech.filatov.pigeoner.dto.SectionDto;
import tech.filatov.pigeoner.repository.SectionRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sections")
public class SectionRestController {

    private final SectionRepository repository;

    public SectionRestController(SectionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/info")
    public List<SectionDto> getTopLevelSectionInfo() {
        return repository.getTopLevelCommonSectionInfo();
    }

    @GetMapping("/info/{id}")
    public List<SectionDto> getSectionInfo(@PathVariable int id) {
        return repository.getCommonSectionInfoById(id);
    }
}
