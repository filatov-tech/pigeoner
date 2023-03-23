package tech.filatov.pigeoner.web.pigeon;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.filatov.pigeoner.dto.PigeonTableDto;
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
}
