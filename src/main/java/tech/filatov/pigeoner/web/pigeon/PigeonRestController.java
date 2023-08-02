package tech.filatov.pigeoner.web.pigeon;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.filatov.pigeoner.AuthorizedUser;
import tech.filatov.pigeoner.dto.FilterParams;
import tech.filatov.pigeoner.dto.PigeonShallowDto;
import tech.filatov.pigeoner.dto.PigeonDto;
import tech.filatov.pigeoner.service.PigeonService;

import java.util.List;

import static tech.filatov.pigeoner.util.ValidationUtil.validateDataFromFilter;

@RestController
@RequestMapping("/api/v1/pigeons")
public class PigeonRestController {

    private final AuthorizedUser authUser = new AuthorizedUser();
    private final PigeonService service;

    public PigeonRestController(PigeonService service) {
        this.service = service;
    }

    @GetMapping
    public List<PigeonShallowDto> getAll() {
        return service.getAll(authUser.getId());
    }

    @PostMapping("/filter")
    public ResponseEntity<List<PigeonShallowDto>> getFiltered(@RequestBody FilterParams params) {
        validateDataFromFilter(params);
        return ResponseEntity.ok(service.getAll(params, authUser.getId()));
    }

    @GetMapping("/{id}/with-ancestors")
    public PigeonDto getWithAncestors(@PathVariable int id) {
        return service.getWithAncestorsAndFlights(id, authUser.getId());
    }
}
