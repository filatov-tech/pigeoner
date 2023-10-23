package tech.filatov.pigeoner.web.pigeon;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.filatov.pigeoner.dto.ColorDto;
import tech.filatov.pigeoner.model.User;
import tech.filatov.pigeoner.service.ColorService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static tech.filatov.pigeoner.util.ValidationUtil.assureIdConsistent;
import static tech.filatov.pigeoner.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(ColorRestController.REST_URL)
public class ColorRestController {
    static final String REST_URL = "/api/v1/color";

    private final ColorService service;

    public ColorRestController(ColorService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ColorDto get(@PathVariable long id, @AuthenticationPrincipal User authUser) {
        return service.get(id, authUser.getId());
    }

    @GetMapping
    public List<ColorDto> getAll(@AuthenticationPrincipal User authUser) {
        return service.getAll(authUser.getId());
    }

    @PostMapping
    public ResponseEntity<ColorDto> create(
            @Valid @RequestBody ColorDto color,
            @AuthenticationPrincipal User authUser
    ) {
        checkNew(color);
        ColorDto created = service.create(color, authUser.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/{id}")
    public ColorDto update(
            @Valid @RequestBody ColorDto color,
            @PathVariable long id,
            @AuthenticationPrincipal User authUser
    ) {
        assureIdConsistent(color, id);
        return service.update(color, id, authUser.getId());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id, @AuthenticationPrincipal User authUser) {
        service.delete(id, authUser.getId());
    }
}
