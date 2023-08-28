package tech.filatov.pigeoner.repository.flight;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
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
}
