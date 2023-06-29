package tech.filatov.pigeoner.web.flight;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.filatov.pigeoner.dto.FlightTableDto;
import tech.filatov.pigeoner.service.FlightService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightRestController {

    private final long id = 100000;
    private final FlightService service;

    public FlightRestController(FlightService service) {
        this.service = service;
    }

    @GetMapping
    public List<FlightTableDto> getAll() {
        return service.getAllWithNumberOfMyPigeons(id);
    }
}
