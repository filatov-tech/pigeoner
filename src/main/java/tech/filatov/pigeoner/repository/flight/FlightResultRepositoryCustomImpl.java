package tech.filatov.pigeoner.repository.flight;

import org.springframework.stereotype.Repository;
import tech.filatov.pigeoner.model.Keeper;
import tech.filatov.pigeoner.model.Keeper_;
import tech.filatov.pigeoner.model.flight.*;
import tech.filatov.pigeoner.model.pigeon.Pigeon;
import tech.filatov.pigeoner.model.pigeon.Pigeon_;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class FlightResultRepositoryCustomImpl implements FlightResultRepositoryCustom {

    private final EntityManager em;

    public FlightResultRepositoryCustomImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Map<Long, Integer> getNumberOfMyParticipantsForEveryFlight(long userId) {
        return em.createQuery("""
            SELECT fr.flight.id AS flightId, count(fr) AS myParticipantsNumber
            FROM FlightResult fr
            LEFT OUTER JOIN Pigeon p ON fr.pigeon.id = p.id
            WHERE fr.owner.id = :userId AND p.isOwn IS TRUE
            GROUP BY fr.flight.id
        """, Tuple.class)
                .setParameter("userId", userId)
                .getResultStream()
                .collect(Collectors.toMap(
                        tuple -> ((Number) tuple.get("flightId")).longValue(),
                        tuple -> ((Number) tuple.get("myParticipantsNumber")).intValue()
                ));
    }

    @Override
    public List<FlightResult> findAllByFlightIdAndKeeperId(long flightId, long keeperId, long userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FlightResult> cq = cb.createQuery(FlightResult.class);

        Root<FlightResult> flightResultRoot = cq.from(FlightResult.class);

        Join<FlightResult, Pigeon> pigeon = flightResultRoot.join(FlightResult_.pigeon);
        Join<Pigeon, Keeper> keeper = pigeon.join(Pigeon_.keeper);
        keeper.on(cb.equal(keeper.get(Keeper_.id), keeperId));

        cq.where(
                cb.and(
                    cb.equal(flightResultRoot.get(FlightResult_.flight), flightId),
                    cb.equal(flightResultRoot.get(FlightResult_.owner), userId)
                )
        );
        TypedQuery<FlightResult> executableQuery = em.createQuery(cq);
        return executableQuery.getResultList();
    }

    @Override
    public List<FlightResult> getAllByLaunchPoint(long launchPointId, long userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FlightResult> cq = cb.createQuery(FlightResult.class);

        Root<FlightResult> flightResultRoot = cq.from(FlightResult.class);
        Join<FlightResult, Flight> flight = flightResultRoot.join(FlightResult_.flight);
        flight.on(cb.equal(flight.get(Flight_.launchPoint), launchPointId));

        cq.where(cb.equal(flightResultRoot.get(FlightResult_.owner), userId));
        TypedQuery<FlightResult> executableQuery = em.createQuery(cq);
        return executableQuery.getResultList();
    }
}
