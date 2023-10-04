package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.filatov.pigeoner.dto.LaunchPointDto;
import tech.filatov.pigeoner.model.Keeper;
import tech.filatov.pigeoner.model.flight.LaunchPoint;
import tech.filatov.pigeoner.model.flight.PreciseDistance;
import tech.filatov.pigeoner.repository.flight.LaunchPointRepository;
import tech.filatov.pigeoner.util.exception.IllegalRequestDataException;
import tech.filatov.pigeoner.util.exception.NotFoundException;

import java.util.*;

import static tech.filatov.pigeoner.util.LaunchPointUtil.*;
import static tech.filatov.pigeoner.util.ValidationUtil.*;

@Service
public class LaunchPointService {
    private final LaunchPointRepository repository;
    private final KeeperService keeperService;
    private final PreciseDistanceService preciseDistanceService;
    private final UserService userService;

    public LaunchPointService(LaunchPointRepository repository, KeeperService keeperService, PreciseDistanceService preciseDistanceService, UserService userService) {
        this.repository = repository;
        this.keeperService = keeperService;
        this.preciseDistanceService = preciseDistanceService;
        this.userService = userService;
    }

    public LaunchPoint getOne(long id, long userId) {
        return repository.findOneByIdAndOwnerId(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public LaunchPointDto getOneDto(long id, long userId) {
        LaunchPointDto launchPointDto =
                repository.findOneDto(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
        launchPointDto.setMainKeeperPreciseDistance(
                preciseDistanceService.get(keeperService.getMainKeeperDto(userId).getId(), id).getPreciseDistance()
        );
        return launchPointDto;
    }

    public List<LaunchPoint> getAll(long userId) {
        return repository.findAllByOwnerId(userId);
    }

    public List<LaunchPointDto> getAllDto(long userId) {
        Keeper mainKeeper = keeperService.getMainWithPreciseDistances(userId);
        List<LaunchPointDto> launchPointDtos = repository.findAllDtoByOwnerId(userId);
        addPreciseDistancesInfoTo(launchPointDtos, mainKeeper);
        return launchPointDtos;
    }

    @Transactional
    public LaunchPointDto createOrUpdate(LaunchPointDto dto, long userId) {
        LaunchPoint launchPoint = instantiateFrom(dto, userId);
        return save(launchPoint, userId);
    }

    protected LaunchPointDto save(LaunchPoint launchPoint, long userId) {
        launchPoint = repository.save(launchPoint);
        //noinspection ConstantConditions
        return getOneDto(launchPoint.getId(), userId);
    }

    @Transactional
    public void delete(long id, long userId) {
        if (repository.getFlightsNumberWithLaunchPoint(id, userId) > 0) {
            throw new IllegalRequestDataException("Невозможно удалить - данная точка запуска используется в вылетах");
        }
        checkNotFoundWithId(repository.delete(id, userId) != 0, id);
    }

    private LaunchPoint instantiateFrom(LaunchPointDto dto, long userId) {
        LaunchPoint launchPoint;
        if (dto.isNew()) {
            launchPoint = makeInstanceFrom(dto);
            launchPoint.setOwner(userService.get(userId));
        } else {
            launchPoint = fillWithUpdatedFields(getOne(dto.getId(), userId), dto);
        }
        if (dto.getMainKeeperPreciseDistance() != null) {
            Keeper keeper = keeperService.get(
                    keeperService.getMainKeeperDto(userId).getId(),
                    userId
            );
            preciseDistanceService.save(dto.getMainKeeperPreciseDistance(), keeper, launchPoint);
        }
        return launchPoint;
    }

    private void addPreciseDistancesInfoTo(List<LaunchPointDto> launchPointDtos, Keeper keeper) {
        Map<Long, Double> preciseDistancesMap = new HashMap<>();
        for (PreciseDistance preciseDistance : keeper.getPreciseDistances()) {
            preciseDistancesMap.put(
                    preciseDistance.getLaunchPoint().getId(),
                    preciseDistance.getPreciseDistance()
            );
        }
        for (LaunchPointDto launchPoint : launchPointDtos) {
            launchPoint.setMainKeeperPreciseDistance(
                    preciseDistancesMap.get(launchPoint.getId())
            );
        }
    }
}
