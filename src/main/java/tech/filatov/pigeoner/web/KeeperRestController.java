package tech.filatov.pigeoner.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.filatov.pigeoner.AuthorizedUser;
import tech.filatov.pigeoner.dto.KeeperDto;
import tech.filatov.pigeoner.repository.KeeperRepository;
import tech.filatov.pigeoner.service.KeeperService;
import tech.filatov.pigeoner.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static tech.filatov.pigeoner.util.ValidationUtil.assureIdConsistent;
import static tech.filatov.pigeoner.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(KeeperRestController.REST_URL)
public class KeeperRestController {
    public static final String REST_URL = "/api/v1/keepers";

    private final AuthorizedUser authUser = new AuthorizedUser();
    private final KeeperService service;

    public KeeperRestController(KeeperService service) {
        this.service = service;
    }

    @GetMapping
    public List<KeeperDto> getAll() {
        return service.getAllDto(authUser.getId());
    }

    @GetMapping("/{keeperId}")
    public KeeperDto get(@PathVariable long keeperId) {
        return service.getDto(keeperId, authUser.getId());
    }

    @GetMapping("/main")
    public KeeperDto getMain() {
        return service.getMainKeeperDto(authUser.getId());
    }

    @PostMapping
    public ResponseEntity<KeeperDto> create(@Valid @RequestBody KeeperDto dto) {
        checkNew(dto);
        KeeperDto created = service.createOrUpdate(dto, authUser.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{keeperId}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/{keeperId}")
    public KeeperDto update(@Valid @RequestBody KeeperDto dto, @PathVariable Long keeperId) {
        assureIdConsistent(dto, keeperId);
        return service.createOrUpdate(dto, keeperId);
    }

    @DeleteMapping("/{keeperId}")
    public void delete(@PathVariable long keeperId) {
        service.delete(keeperId, authUser.getId());
    }
}
