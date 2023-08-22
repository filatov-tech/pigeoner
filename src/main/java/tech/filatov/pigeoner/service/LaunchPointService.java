package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.filatov.pigeoner.dto.LaunchPointDto;
import tech.filatov.pigeoner.model.flight.LaunchPoint;
import tech.filatov.pigeoner.repository.flight.LaunchPointRepository;
import tech.filatov.pigeoner.util.exception.NotFoundException;

import java.util.List;

import static tech.filatov.pigeoner.util.LaunchPointUtil.*;
import static tech.filatov.pigeoner.util.ValidationUtil.*;

@Service
public class LaunchPointService {
    private final LaunchPointRepository repository;
    private final UserService userService;

    public LaunchPointService(LaunchPointRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public LaunchPoint getOne(long id, long userId) {
        return repository.findOneByIdAndOwnerId(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public LaunchPointDto getOneDto(long id, long userId) {
        return repository.findOneDto(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public List<LaunchPoint> getAll(long userId) {
        return repository.findAllByOwnerId(userId);
    }

    public List<LaunchPointDto> getAllDto(long userId) {
        return repository.findAllDtoByOwnerId(userId);
    }

    @Transactional
    public LaunchPointDto createOrUpdate(LaunchPointDto dto, long userId) {
        LaunchPoint launchPoint = instantiateFrom(dto, userId);
        return save(launchPoint, userId);
    }

    private LaunchPoint instantiateFrom(LaunchPointDto dto, long userId) {
        LaunchPoint launchPoint;
        if (dto.isNew()) {
            launchPoint = makeInstanceFrom(dto);
            launchPoint.setOwner(userService.get(userId));
        } else {
            launchPoint = fillWithUpdatedFields(getOne(dto.getId(), userId), dto);
        }
        return launchPoint;
    }

    protected LaunchPointDto save(LaunchPoint launchPoint, long userId) {
        launchPoint = repository.save(launchPoint);
        //noinspection ConstantConditions
        return getOneDto(launchPoint.getId(), userId);
    }

    @Transactional
    public void delete(long id, long userId) {
        checkNotFoundWithId(repository.delete(id, userId) != 0, id);
    }
}
