package tech.filatov.pigeoner.web.flight;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.filatov.pigeoner.dto.FlightDto;
import tech.filatov.pigeoner.model.User;
import tech.filatov.pigeoner.service.FlightService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static tech.filatov.pigeoner.util.ValidationUtil.assureIdConsistent;
import static tech.filatov.pigeoner.util.ValidationUtil.checkNew;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightRestController {
    public static final String REST_URL = "/api/v1/flights";

    private final FlightService service;

    public FlightRestController(FlightService service) {
        this.service = service;
    }

    @GetMapping
    public List<FlightDto> getAll(@AuthenticationPrincipal User authUser) {
        return service.getAllDto(authUser.getId());
    }

    @GetMapping("/{id}")
    public FlightDto getById(@PathVariable long id, @AuthenticationPrincipal User authUser) {
        return service.getDto(id, authUser.getId());
    }

    @PostMapping
    public ResponseEntity<FlightDto> create(
            @Valid @RequestBody FlightDto flightDto,
            @AuthenticationPrincipal User authUser
    ) {
        checkNew(flightDto);
        FlightDto created = service.create(flightDto, authUser.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/{id}")
    public FlightDto update(
            @Valid @RequestBody FlightDto flightDto,
            @PathVariable long id,
            @AuthenticationPrincipal User authUser
    ) {
        assureIdConsistent(flightDto, id);
        return service.update(flightDto, authUser.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id, @AuthenticationPrincipal User authUser) {
        service.delete(id, authUser.getId());
    }

}
