package tech.filatov.pigeoner.web.flight;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.filatov.pigeoner.AuthorizedUser;
import tech.filatov.pigeoner.dto.FlightResultDto;
import tech.filatov.pigeoner.repository.FlightResultRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class FlightResultRestController {

    private final AuthorizedUser authUser = new AuthorizedUser();
    private final FlightResultRepository repository;

    public FlightResultRestController(FlightResultRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/flights/{id}/flight-results")
    public List<FlightResultDto> getAllByFlightId(@PathVariable long id) {
        return repository.getAllByFlightId(id, authUser.getId());
    }
}
