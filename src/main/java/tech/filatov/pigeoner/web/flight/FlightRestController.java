package tech.filatov.pigeoner.web.flight;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.filatov.pigeoner.AuthorizedUser;
import tech.filatov.pigeoner.dto.FlightDto;
import tech.filatov.pigeoner.service.FlightService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightRestController {

    private final AuthorizedUser authUser = new AuthorizedUser();
    private final FlightService service;

    public FlightRestController(FlightService service) {
        this.service = service;
    }

    @GetMapping
    public List<FlightDto> getAll() {
        return service.getAllFlightDto(authUser.getId());
    }

    @GetMapping("/{id}")
    public FlightDto getById(@PathVariable long id) {
        return service.getFlightDto(id, authUser.getId());
    }
}
