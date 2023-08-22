package tech.filatov.pigeoner.web.flight;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.filatov.pigeoner.AuthorizedUser;
import tech.filatov.pigeoner.dto.FlightResultDto;
import tech.filatov.pigeoner.service.FlightResultService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class FlightResultRestController {

    private final AuthorizedUser authUser = new AuthorizedUser();
    private final FlightResultService service;

    public FlightResultRestController(FlightResultService service) {
        this.service = service;
    }

    @GetMapping("/flights/{id}/flight-results")
    public List<FlightResultDto> getAllByFlightId(@PathVariable long id) {
        return service.getAllByFlightId(id, authUser.getId());
    }
}
