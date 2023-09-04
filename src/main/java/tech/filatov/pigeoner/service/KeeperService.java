package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.filatov.pigeoner.dto.KeeperDto;
import tech.filatov.pigeoner.model.Keeper;
import tech.filatov.pigeoner.repository.KeeperRepository;
import tech.filatov.pigeoner.util.ValidationUtil;
import tech.filatov.pigeoner.util.exception.NotFoundException;

import java.util.List;

import static tech.filatov.pigeoner.util.ValidationUtil.checkNotFoundWithId;

@Service
public class KeeperService {
    private final KeeperRepository repository;
    private final UserService userService;

    public KeeperService(KeeperRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public Keeper get(long id, long userId) {
        return repository.findByIdAndOwnerId(id, userId)
                .orElseThrow(NotFoundException.withIdInfo(id));
    }

    public KeeperDto getDto(long id, long userId) {
        return repository.getDto(id, userId);
    }

    public List<KeeperDto> getAllDto(long userId) {
        return repository.getAllDto(userId);
    }

    public KeeperDto getMainKeeperDto(long userId) {
        return repository.getMainKeeperDto(userId);
    }

    @Transactional
    public KeeperDto createOrUpdate(KeeperDto dto, long userId) {
        Keeper keeper;
        if (dto.isNew()) {
            keeper = new Keeper();
            keeper.setOwner(userService.getRef(userId));
        } else {
            keeper = get(dto.getId(), userId);
        }
        keeper.setName(dto.getName());
        keeper = repository.save(keeper);
        //noinspection ConstantConditions
        return getDto(keeper.getId(), userId);
    }

    @Transactional
    public void delete(long id, long userId) {
        checkNotFoundWithId(repository.delete(id, userId) != 0, id);
    }
}
