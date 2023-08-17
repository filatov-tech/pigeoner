package tech.filatov.pigeoner.web.pigeon;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.filatov.pigeoner.AuthorizedUser;
import tech.filatov.pigeoner.dto.ColorDto;
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
    private final AuthorizedUser authUser = new AuthorizedUser();

    public ColorRestController(ColorService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ColorDto get(@PathVariable long id) {
        return service.get(id, authUser.getId());
    }

    @GetMapping
    public List<ColorDto> getAll() {
        return service.getAll(authUser.getId());
    }

    @PostMapping
    public ResponseEntity<ColorDto> create(@Valid @RequestBody ColorDto color) {
        checkNew(color);
        ColorDto created = service.create(color, authUser.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/{id}")
    public ColorDto update(@Valid @RequestBody ColorDto color, @PathVariable long id) {
        assureIdConsistent(color, id);
        return service.update(color, id, authUser.getId());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.delete(id, authUser.getId());
    }

}
