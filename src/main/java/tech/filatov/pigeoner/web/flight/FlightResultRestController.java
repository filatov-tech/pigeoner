package tech.filatov.pigeoner.web.flight;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.filatov.pigeoner.dto.FlightResultDto;
import tech.filatov.pigeoner.model.User;
import tech.filatov.pigeoner.service.FlightResultService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static tech.filatov.pigeoner.util.ValidationUtil.assureIdConsistent;
import static tech.filatov.pigeoner.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(FlightResultRestController.REST_URL)
public class FlightResultRestController {
    public static final String REST_URL = "/api/v1/flights/{flightId}/flight-results";
    private final FlightResultService service;

    public FlightResultRestController(FlightResultService service) {
        this.service = service;
    }

    @GetMapping
    public List<FlightResultDto> getAllByFlightId(
            @PathVariable long flightId,
            @AuthenticationPrincipal User authUser
    ) {
        return service.getAllDtoByFlightId(flightId, authUser.getId());
    }

    @PostMapping
    public ResponseEntity<FlightResultDto> create(
            @Valid @RequestBody FlightResultDto dto,
            @PathVariable long flightId,
            @AuthenticationPrincipal User authUser
    ) {
        checkNew(dto);
        FlightResultDto created = service.saveOrUpdate(dto, flightId, authUser.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{flightResultId}")
                .buildAndExpand(flightId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/{flightResultId}")
    public FlightResultDto update(
            @Valid @RequestBody FlightResultDto dto,
            @PathVariable long flightId,
            @PathVariable long flightResultId,
            @AuthenticationPrincipal User authUser
    ) {
        assureIdConsistent(dto, flightResultId);
        return service.saveOrUpdate(dto,flightId, authUser.getId());
    }

    @DeleteMapping("/{flightResultId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable long flightId,
            @PathVariable long flightResultId,
            @AuthenticationPrincipal User authUser
    ) {
        service.delete(flightResultId, authUser.getId());
    }
}
