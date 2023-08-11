package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.model.Keeper;
import tech.filatov.pigeoner.repository.KeeperRepository;
import tech.filatov.pigeoner.util.exception.NotFoundException;

@Service
public class KeeperService {
    private final KeeperRepository repository;

    public KeeperService(KeeperRepository repository) {
        this.repository = repository;
    }

    public Keeper get(long id, long userId) {
        return repository.findByIdAndOwnerId(id, userId)
                .orElseThrow(NotFoundException.withIdInfo(id));
    }
}
