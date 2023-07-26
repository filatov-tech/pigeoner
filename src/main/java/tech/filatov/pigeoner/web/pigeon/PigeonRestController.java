package tech.filatov.pigeoner.web.pigeon;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.filatov.pigeoner.AuthorizedUser;
import tech.filatov.pigeoner.dto.FilterParams;
import tech.filatov.pigeoner.dto.PigeonTableDto;
import tech.filatov.pigeoner.dto.PigeonWithAncestorsDto;
import tech.filatov.pigeoner.service.PigeonService;

import java.util.List;
import java.util.Map;

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
    public List<PigeonTableDto> getAll() {
        return service.getAll();
    }

    @PostMapping("/filter")
    public ResponseEntity<List<PigeonTableDto>> getFiltered(@RequestBody FilterParams params) {
        validateDataFromFilter(params);
        return ResponseEntity.ok(service.getAll(params));
    }

    @GetMapping("/{id}/with-ancestors")
    public PigeonWithAncestorsDto getWithAncestors(@PathVariable int id) {
        return service.getWithAncestorsAndFlights(id, authUser.getId());
    }
}
