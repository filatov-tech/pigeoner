package tech.filatov.pigeoner.web.pigeon;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.filatov.pigeoner.AuthorizedUser;
import tech.filatov.pigeoner.dto.FilterParams;
import tech.filatov.pigeoner.dto.PigeonShallowDto;
import tech.filatov.pigeoner.dto.PigeonDto;
import tech.filatov.pigeoner.service.PigeonService;

import java.net.URI;
import java.util.List;

import static tech.filatov.pigeoner.util.ValidationUtil.validateDataFromFilter;

@RestController
@RequestMapping(PigeonRestController.REST_URL)
public class PigeonRestController {
    static final String REST_URL = "/api/v1/pigeons";

    private final AuthorizedUser authUser = new AuthorizedUser();
    private final PigeonService service;

    public PigeonRestController(PigeonService service) {
        this.service = service;
    }

    @GetMapping
    public List<PigeonShallowDto> getAll() {
        return service.getAll(authUser.getId());
    }

    @GetMapping("/{id}")
    public PigeonDto get(@PathVariable long id) {
        return service.getPigeonDto(id, authUser.getId());
    }

    @PostMapping("/filter")
    public ResponseEntity<List<PigeonShallowDto>> getFiltered(@RequestBody FilterParams params) {
        validateDataFromFilter(params);
        return ResponseEntity.ok(service.getAllFiltered(params, authUser.getId()));
    }

    @GetMapping("/{id}/with-ancestors")
    public PigeonDto getWithAncestors(@PathVariable long id) {
        return service.getWithAncestors(id, authUser.getId());
    }

    @PostMapping
    public ResponseEntity<PigeonDto> create(@RequestBody PigeonShallowDto pigeon) {
        PigeonDto created = service.create(pigeon, authUser.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
