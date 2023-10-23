package tech.filatov.pigeoner.web.section;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.filatov.pigeoner.dto.SectionDto;
import tech.filatov.pigeoner.model.User;
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

    private final SectionRepository repository;
    private final SectionService service;

    public SectionRestController(SectionRepository repository, SectionService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/{id}")
    public SectionDto get(@PathVariable long id, @AuthenticationPrincipal User authUser) {
        return service.getDto(id, authUser.getId());
    }

    @GetMapping
    public List<SectionDto> getAll(@AuthenticationPrincipal User authUser) {
        return service.getAllWithFullAddress(authUser.getId());
    }

    @GetMapping("/hierarchical")
    public List<SectionDto> getAllHierarchical(@AuthenticationPrincipal User authUser) {
        return service.getAllHierarchical(authUser.getId());
    }

    @GetMapping("/hierarchical-with-pigeons")
    public List<SectionDto> getAllHierarchicalWithPigeons(@AuthenticationPrincipal User authUser) {
        return service.getSectionsDtoTreeWithPigeons(authUser.getId());
    }

    @PostMapping
    public ResponseEntity<SectionDto> create(
            @Valid @RequestBody SectionDto section,
            @AuthenticationPrincipal User authUser
    ) {
        checkNew(section);
        SectionDto created = service.createOrUpdate(section, authUser.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/{id}")
    public SectionDto update(
            @Valid @RequestBody SectionDto section,
            @PathVariable long id,
            @AuthenticationPrincipal User authUser
    ) {
        assureIdConsistent(section, id);
        return service.createOrUpdate(section, authUser.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id, @AuthenticationPrincipal User authUser) {
        service.delete(id, authUser.getId());
    }

    @GetMapping("/info")
    public List<SectionDto> getTopLevelSectionInfo(@AuthenticationPrincipal User authUser) {
        return repository.getTopLevelCommonSectionInfo(authUser.getId());
    }

    @GetMapping("/info/{id}")
    public List<SectionDto> getSectionInfo(@PathVariable int id, @AuthenticationPrincipal User authUser) {
        return repository.getCommonSectionInfoById(id, authUser.getId());
    }

    @GetMapping("/hierarchy")
    public List<SectionDto> getHierarchicalStructure(@AuthenticationPrincipal User authUser) {
        return service.getAllHierarchicalWithoutNests(authUser.getId());
    }
}
