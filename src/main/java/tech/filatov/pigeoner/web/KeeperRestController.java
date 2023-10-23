package tech.filatov.pigeoner.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.filatov.pigeoner.dto.KeeperDto;
import tech.filatov.pigeoner.model.User;
import tech.filatov.pigeoner.service.KeeperService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static tech.filatov.pigeoner.util.ValidationUtil.assureIdConsistent;
import static tech.filatov.pigeoner.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(KeeperRestController.REST_URL)
public class KeeperRestController {
    public static final String REST_URL = "/api/v1/keepers";

    private final KeeperService service;

    public KeeperRestController(KeeperService service) {
        this.service = service;
    }

    @GetMapping
    public List<KeeperDto> getAll(@AuthenticationPrincipal User authUser) {
        return service.getAllDto(authUser.getId());
    }

    @GetMapping("/{keeperId}")
    public KeeperDto get(@PathVariable long keeperId, @AuthenticationPrincipal User authUser) {
        return service.getDto(keeperId, authUser.getId());
    }

    @GetMapping("/main")
    public KeeperDto getMain(@AuthenticationPrincipal User authUser) {
        return service.getMainKeeperDto(authUser.getId());
    }

    @PostMapping
    public ResponseEntity<KeeperDto> create(
            @Valid @RequestBody KeeperDto dto,
            @AuthenticationPrincipal User authUser
    ) {
        checkNew(dto);
        KeeperDto created = service.createOrUpdate(dto, authUser.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{keeperId}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/{keeperId}")
    public KeeperDto update(
            @Valid @RequestBody KeeperDto dto,
            @PathVariable Long keeperId,
            @AuthenticationPrincipal User authUser
    ) {
        assureIdConsistent(dto, keeperId);
        return service.createOrUpdate(dto, authUser.getId());
    }

    @DeleteMapping("/{keeperId}")
    public void delete(@PathVariable long keeperId, @AuthenticationPrincipal User authUser) {
        service.delete(keeperId, authUser.getId());
    }
}
