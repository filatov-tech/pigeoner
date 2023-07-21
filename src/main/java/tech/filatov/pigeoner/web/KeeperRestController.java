package tech.filatov.pigeoner.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.filatov.pigeoner.AuthorizedUser;
import tech.filatov.pigeoner.dto.KeeperDto;
import tech.filatov.pigeoner.repository.KeeperRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/keepers")
public class KeeperRestController {

    private final AuthorizedUser authUser = new AuthorizedUser();
    private KeeperRepository repository;

    public KeeperRestController(KeeperRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<KeeperDto> getAll() {
        return repository.getAll(authUser.getId());
    }

    @GetMapping("/{id}")
    public KeeperDto get(@PathVariable long id) {
        return repository.get(id, authUser.getId());
    }

    @GetMapping("/main")
    public KeeperDto getMain() {
        return repository.get(authUser.getId());
    }

}
