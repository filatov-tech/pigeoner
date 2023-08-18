package tech.filatov.pigeoner.web.section;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.filatov.pigeoner.AuthorizedUser;
import tech.filatov.pigeoner.dto.SectionDto;
import tech.filatov.pigeoner.repository.SectionRepository;
import tech.filatov.pigeoner.service.SectionService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static tech.filatov.pigeoner.util.ValidationUtil.assureIdConsistent;
import static tech.filatov.pigeoner.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(SectionRestController.REST_URL)
public class SectionRestController {
    public static final String REST_URL = "/api/v1/sections";

    private final AuthorizedUser authUser = new AuthorizedUser();
    private final SectionRepository repository;
    private final SectionService service;

    public SectionRestController(SectionRepository repository, SectionService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/{id}")
    public SectionDto get(@PathVariable long id) {
        return service.getDto(id, authUser.getId());
    }

    @GetMapping
    public List<SectionDto> getSectionsData() {
        return service.getSectionsDtoTreeWithPigeons(authUser.getId());
    }

    @PostMapping
    public ResponseEntity<SectionDto> create(@Valid @RequestBody SectionDto section) {
        checkNew(section);
        SectionDto created = service.createOrUpdate(section, authUser.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/{id}")
    public SectionDto update(@Valid @RequestBody SectionDto section, @PathVariable long id) {
        assureIdConsistent(section, id);
        return service.createOrUpdate(section, authUser.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        service.delete(id, authUser.getId());
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
        return service.getDtoHierarchicalStructure();
    }
}
