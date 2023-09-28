package tech.filatov.pigeoner.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.filatov.pigeoner.dto.FlightDto;
import tech.filatov.pigeoner.model.flight.Flight;
import tech.filatov.pigeoner.repository.flight.FlightRepository;
import tech.filatov.pigeoner.util.FlightMapper;
import tech.filatov.pigeoner.util.ValidationUtil;
import tech.filatov.pigeoner.util.exception.NotFoundException;

import java.util.List;
import java.util.Map;

@Service
public class FlightService {
    private final FlightRepository repository;
    private final FlightResultService flightResultService;
    private final LaunchPointService launchPointService;
    private final UserService userService;
    private final FlightMapper mapper;

    public FlightService(FlightRepository repository, @Lazy FlightResultService flightResultService, FlightMapper mapper, UserService userService, LaunchPointService launchPointService) {
        this.repository = repository;
        this.flightResultService = flightResultService;
        this.mapper = mapper;
        this.userService = userService;
        this.launchPointService = launchPointService;
    }

    public List<FlightDto> getAllDto(long userId) {
        List<FlightDto> dtos = repository.getAllDto(userId);
        Map<Long, Integer> myParticipants = flightResultService.getNumberOfMyParticipantsForEveryFlight(userId);
        for (FlightDto dto : dtos) {
            dto.setNumberParticipants(myParticipants.get(dto.getId()));
        }
        return dtos;
    }

    public FlightDto getDto(long id, long userId) {
        FlightDto flightDto = repository.findDtoById(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
        flightDto.setNumberParticipants(flightResultService.getNumberOfMyParticipantsByFlightId(id, userId));
        return flightDto;
    }

    public Flight getOne(long id, long userId) {
        return repository.findOneByIdAndOwnerId(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    @Transactional
    public FlightDto saveOrUpdate(FlightDto dto, long userId) {
        Flight flight = instantiateFrom(dto, userId);
        flight.setLaunchPoint(launchPointService.getOne(dto.getLaunchPointId(), userId));
        flight = save(flight);
        //noinspection ConstantConditions
        return getDto(flight.getId(), userId);
    }

    @Transactional
    public void delete(long id, long userId) {
        ValidationUtil.checkNotFoundWithId(repository.delete(id, userId) != 0, id);
    }

    private Flight instantiateFrom(FlightDto dto, long userId) {
        Flight flight;
        if (dto.isNew()) {
            flight = mapper.instantiateFrom(dto);
            flight.setOwner(userService.get(userId));
        } else {
            flight = mapper.fillInstantiatedWith(dto, getOne(dto.getId(), userId));
        }
        return flight;
    }

    protected Flight save(Flight flight) {
        return repository.save(flight);
    }
}
