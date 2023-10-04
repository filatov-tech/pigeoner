package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.model.Keeper;
import tech.filatov.pigeoner.model.flight.LaunchPoint;
import tech.filatov.pigeoner.model.flight.PreciseDistance;
import tech.filatov.pigeoner.repository.flight.PreciseDistanceRepository;

@Service
public class PreciseDistanceService {
    private final PreciseDistanceRepository repository;

    public PreciseDistanceService(PreciseDistanceRepository repository) {
        this.repository = repository;
    }

    public PreciseDistance get(long keeperId, long launchPointId) {
        return repository.get(keeperId, launchPointId);
    }

    public PreciseDistance save(Double preciseDistance, Keeper keeper, LaunchPoint launchPoint) {
        return repository.save(
                new PreciseDistance(preciseDistance, keeper, launchPoint)
        );
    }
}
