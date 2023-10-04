package tech.filatov.pigeoner.repository.flight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.model.flight.PreciseDistance;
import tech.filatov.pigeoner.model.flight.PreciseDistanceId;

public interface PreciseDistanceRepository extends JpaRepository<PreciseDistance, PreciseDistanceId> {
    @Query("""
        SELECT pd
        FROM PreciseDistance pd
        WHERE pd.keeper.id = :keeperId AND pd.launchPoint.id = :launchPointId
    """)
    PreciseDistance get(long keeperId, long launchPointId);
}
