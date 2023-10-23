package tech.filatov.pigeoner.web.pigeon;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.filatov.pigeoner.dto.FilterParams;
import tech.filatov.pigeoner.dto.PigeonDto;
import tech.filatov.pigeoner.dto.PigeonShallowDto;
import tech.filatov.pigeoner.model.User;
import tech.filatov.pigeoner.service.PigeonService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static tech.filatov.pigeoner.util.ValidationUtil.*;

@RestController
@RequestMapping(PigeonRestController.REST_URL)
public class PigeonRestController {
    static final String REST_URL = "/api/v1/pigeons";

    private final PigeonService service;

    public PigeonRestController(PigeonService service) {
        this.service = service;
    }

    @GetMapping
    public List<PigeonShallowDto> getAll(@AuthenticationPrincipal User authUser) {
        return service.getAll(authUser.getId());
    }

    @GetMapping("/{id}")
    public PigeonDto get(@PathVariable long id, @AuthenticationPrincipal User authUser) {
        return service.getPigeonDto(id, authUser.getId());
    }

    @PostMapping("/filter")
    public ResponseEntity<List<PigeonShallowDto>> getFiltered(
            @RequestBody FilterParams params,
            @AuthenticationPrincipal User authUser
    ) {
        validateDataFromFilter(params);
        return ResponseEntity.ok(service.getAllFiltered(params, authUser.getId()));
    }

    @GetMapping("/{id}/with-ancestors")
    public PigeonDto getWithAncestors(@PathVariable long id, @AuthenticationPrincipal User authUser) {
        return service.getWithAncestors(id, authUser.getId());
    }

    @PostMapping
    public ResponseEntity<PigeonDto> create(
            @RequestBody @Valid PigeonShallowDto pigeon,
            @AuthenticationPrincipal User authUser
    ) {
         return create(pigeon, null, authUser.getId());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PigeonDto> createWithImages(
            @RequestPart("pigeon") @Valid PigeonShallowDto pigeon,
            @RequestPart("images") MultipartFile[] images,
            @AuthenticationPrincipal User authUser
    ) {
        return create(pigeon, images, authUser.getId());
    }

    @PutMapping("/{id}")
    public PigeonDto update(
            @RequestBody @Valid PigeonShallowDto pigeon,
            @PathVariable long id,
            @AuthenticationPrincipal User authUser
    ) {
        assureIdConsistent(pigeon, id);
        return service.update(pigeon, null, id, authUser.getId());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PigeonDto updateWithImages(
            @RequestPart("pigeon") @Valid PigeonShallowDto pigeon,
            @RequestPart("images") MultipartFile[] images,
            @PathVariable long id,
            @AuthenticationPrincipal User authUser
    ) {
        assureIdConsistent(pigeon, id);
        return service.update(pigeon, images, id, authUser.getId());
    }

    private ResponseEntity<PigeonDto> create(PigeonShallowDto pigeon, MultipartFile[] images, long userId) {
        checkNew(pigeon);
        PigeonDto created = service.create(pigeon, images, userId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id, @AuthenticationPrincipal User authUser) {
        service.delete(id, authUser.getId());
    }
}
