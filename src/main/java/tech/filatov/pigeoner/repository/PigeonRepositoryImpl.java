package tech.filatov.pigeoner.repository;

import org.springframework.stereotype.Repository;
import tech.filatov.pigeoner.model.Pigeon;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

import static tech.filatov.pigeoner.constant.Constants.*;

@Repository
public class PigeonRepositoryImpl implements PigeonRepositoryCustom {

    EntityManager em;

    public PigeonRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Pigeon> getFiltered(Map<String, String> filterParameters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Pigeon> cq = cb.createQuery(Pigeon.class);

        Root<Pigeon> pigeonRoot = cq.from(Pigeon.class);
        cq.select(pigeonRoot);

        if (!filterParameters.get(RING_NUMBER).isEmpty()) {
            cq.where(cb.equal(pigeonRoot.get("ringNumber"), filterParameters.get(RING_NUMBER)));
        }
        if (!filterParameters.get(CONDITION).isEmpty()) {
            cq.where(cb.equal(pigeonRoot.get("condition"), filterParameters.get(CONDITION)));
        }
        if (!filterParameters.get(LOCATION).isEmpty()) {
            cq.where(cb.equal(pigeonRoot.get("location"), filterParameters.get(LOCATION)));
        }
        if (!filterParameters.get(PIGEON_NAME).isEmpty()) {
            cq.where(cb.equal(pigeonRoot.get("name"), filterParameters.get(PIGEON_NAME)));
        }
        TypedQuery<Pigeon> executableQuery = em.createQuery(cq);

        return executableQuery.getResultList();
    }
}
