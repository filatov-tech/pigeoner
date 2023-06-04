package tech.filatov.pigeoner.web.pigeon;

import org.springframework.web.bind.annotation.*;
import tech.filatov.pigeoner.dto.PigeonTableDto;
import tech.filatov.pigeoner.dto.PigeonWithAncestorsDto;
import tech.filatov.pigeoner.service.PigeonService;

import java.util.List;
import java.util.Map;

import static tech.filatov.pigeoner.util.ValidationUtil.validateDataFromFilter;

@RestController
@RequestMapping("/api/v1/pigeons")
public class PigeonRestController {

    private final PigeonService service;

    public PigeonRestController(PigeonService service) {
        this.service = service;
    }

    @GetMapping
    public List<PigeonTableDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/filter")
    public List<PigeonTableDto> getFiltered(@RequestParam(required = false) Map<String, String> filterParameters) {
        validateDataFromFilter(filterParameters);
        return service.getAll(filterParameters);
    }

    @GetMapping("/{id}/with-ancestors")
    public PigeonWithAncestorsDto getWithAncestors(@PathVariable int id) {
        return service.getWithAncestorsAndFlights(id);
    }
}
