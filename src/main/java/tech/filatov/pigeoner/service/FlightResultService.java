package tech.filatov.pigeoner.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.filatov.pigeoner.dto.FlightResultDto;
import tech.filatov.pigeoner.dto.LaunchPointDto;
import tech.filatov.pigeoner.model.flight.Flight;
import tech.filatov.pigeoner.model.flight.FlightResult;
import tech.filatov.pigeoner.model.flight.FlightType;
import tech.filatov.pigeoner.model.pigeon.Pigeon;
import tech.filatov.pigeoner.repository.flight.FlightResultRepository;
import tech.filatov.pigeoner.util.CommonUtil;
import tech.filatov.pigeoner.util.FlightResultMapper;
import tech.filatov.pigeoner.util.ValidationUtil;
import tech.filatov.pigeoner.util.exception.NotFoundException;

import java.util.List;
import java.util.Map;

@Service
public class FlightResultService {
    private final FlightResultRepository repository;
    private final FlightService flightService;
    private final PigeonService pigeonService;
    private final UserService userService;
    private final FlightResultMapper mapper;

    public FlightResultService(FlightResultRepository repository, @Lazy FlightService flightService, PigeonService pigeonService, UserService userService, FlightResultMapper mapper) {
        this.repository = repository;
        this.flightService = flightService;
        this.pigeonService = pigeonService;
        this.userService = userService;
        this.mapper = mapper;
    }

    public FlightResult get(long id, long userId) {
        return repository.findByIdAndOwnerId(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public List<FlightResult> getAllByFlightId(long flightId, long userId) {
        return repository.findAllByFlightIdAndOwnerId(flightId, userId);
    }

    public List<FlightResult> getAllByFlightIdAndKeeperId(long flightId, long keeperId, long userId) {
        return repository.findAllByFlightIdAndKeeperId(flightId, keeperId, userId);
    }

    public List<FlightResult> getAllByLaunchPointId(long launchPointId, long userId) {
        return repository.getAllByLaunchPoint(launchPointId, userId);
    }

    public FlightResultDto getDto(long id, long userId) {
        return repository.findDto(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public List<FlightResultDto> getAllDtoByPigeonId(long id, long userId) {
        return repository.getAllDtoByPigeonId(id, userId);
    }

    public List<FlightResultDto> getAllDtoByFlightId(long id, long userId) {

        List<FlightResultDto> flightResults = repository.getAllDtoByFlightId(id, userId);
        for (int i = 0; i < flightResults.size(); i++) {
            FlightResultDto flightResult = flightResults.get(i);
            flightResult.setPosition(i + 1);
        }


        return flightResults;
    }

    public Map<Long, Integer> getNumberOfMyParticipantsForEveryFlight(long userId) {
        return repository.getNumberOfMyParticipantsForEveryFlight(userId);
    }

    public int getNumberOfMyParticipantsByFlightId(long id, long userId) {
        return repository.getNumberOfMyParticipantsByFlightId(id, userId);
    }

    @Transactional
    public FlightResultDto saveOrUpdate(FlightResultDto dto, long flightId, long userId) {
        FlightResult target = instantiateFrom(dto, userId);

        Flight flight = flightService.getOne(flightId, userId);
        target.setFlight(flight);

        Pigeon pigeon = pigeonService.get(dto.getPigeonId(), userId);
        target.setPigeon(pigeon);

        if (target.getArrivalTime() != null) {
            Double preciseDistance = extractPreciseDistance(flight, pigeon);
            if (flight.getFlightType() == FlightType.TRAINING || preciseDistance != null) {
                target.setPreciseDistance(extractPreciseDistance(flight, pigeon));
                fillCalculatedFields(target);
            }
        }

        target = repository.save(target);
        //noinspection ConstantConditions
        return getDto(target.getId(), userId);
    }

    @Transactional
    public void delete(long id, long userId) {
        ValidationUtil.checkNotFoundWithId(repository.delete(id, userId) != 0, id);
    }

    @Transactional
    public void recalculateAllFlightResultsBy(LaunchPointDto launchPoint, long userId) {
        List<FlightResult> flightResults = getAllByLaunchPointId(launchPoint.getId(), userId);

        for (FlightResult flightResult : flightResults) {
            flightResult.setPreciseDistance(launchPoint.getMainKeeperPreciseDistance());
            flightResult.setAverageSpeed(CommonUtil.calculateAvgSpeed(flightResult));
        }
        repository.saveAll(flightResults);
    }

    @Transactional
    public void recalculateAllFlightResultsBy(Flight flight, long userId) {
        List<FlightResult> flightResults = getAllByFlightId(flight.getId(), userId);

        for (FlightResult flightResult : flightResults) {
            flightResult.setPreciseDistance(extractPreciseDistance(flightResult.getFlight(), flightResult.getPigeon()));
            flightResult.setAverageSpeed(CommonUtil.calculateAvgSpeed(flightResult));
        }
         repository.saveAll(flightResults);
    }

    private void fillCalculatedFields(FlightResult flightResult) {
        if (flightResult.getArrivalTime() != null) {
            flightResult.setAverageSpeed(CommonUtil.calculateAvgSpeed(flightResult));
        }
    }

    private FlightResult instantiateFrom(FlightResultDto dto, long userId) {
        FlightResult flightResult;
        if (dto.isNew()) {
            flightResult = mapper.instantiateFrom(dto);
            flightResult.setOwner(userService.getRef(userId));
        } else {
            flightResult = mapper.fillInstantiatedWith(dto, get(dto.getId(), userId));
        }
        return flightResult;
    }

    private Double extractPreciseDistance(Flight flight, Pigeon pigeon) {
        return pigeon.getKeeper().getLookupPreciseDistancesMap().get(
                flight.getLaunchPoint().getId()
        );
    }
}
