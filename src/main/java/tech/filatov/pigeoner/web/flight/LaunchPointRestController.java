package tech.filatov.pigeoner.web.flight;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.filatov.pigeoner.dto.LaunchPointDto;
import tech.filatov.pigeoner.model.User;
import tech.filatov.pigeoner.service.LaunchPointService;
import tech.filatov.pigeoner.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(LaunchPointRestController.REST_URL)
public class LaunchPointRestController {
    public static final String REST_URL = "/api/v1/launch-point";

    private final LaunchPointService service;

    public LaunchPointRestController(LaunchPointService service) {
        this.service = service;
    }

    @GetMapping
    public List<LaunchPointDto> getAll(@AuthenticationPrincipal User authUser) {
        return service.getAllDto(authUser.getId());
    }

    @GetMapping("/{id}")
    public LaunchPointDto get(@PathVariable long id, @AuthenticationPrincipal User authUser) {
        return service.getOneDto(id, authUser.getId());
    }

    @PostMapping
    public ResponseEntity<LaunchPointDto> create(
            @Valid @RequestBody LaunchPointDto launchPoint,
            @AuthenticationPrincipal User authUser
    ) {
        ValidationUtil.checkNew(launchPoint);
        LaunchPointDto created = service.createOrUpdate(launchPoint, authUser.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/{id}")
    public LaunchPointDto update(
            @Valid @RequestBody LaunchPointDto launchPoint,
            @PathVariable long id,
            @AuthenticationPrincipal User authUser
    ) {
        ValidationUtil.assureIdConsistent(launchPoint, id);
        return service.createOrUpdate(launchPoint, authUser.getId());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id, @AuthenticationPrincipal User authUser) {
        service.delete(id, authUser.getId());
    }
}
