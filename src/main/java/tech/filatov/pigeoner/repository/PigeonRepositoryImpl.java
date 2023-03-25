package tech.filatov.pigeoner.repository;

import org.springframework.stereotype.Repository;
import tech.filatov.pigeoner.model.Condition;
import tech.filatov.pigeoner.model.Pigeon;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
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


        List<Predicate> predicates = new ArrayList<>();
        if (!filterParameters.get(RING_NUMBER).isEmpty()) {
            predicates.add(cb.equal(pigeonRoot.get(RING_NUMBER), filterParameters.get(RING_NUMBER)));
        }
        if (!filterParameters.get(CONDITION).isEmpty()) {
            predicates.add(
                    cb.equal(
                            pigeonRoot.get("conditionStatus"),
                            Condition.valueOfLabel(filterParameters.get(CONDITION))
                    )
            );
        }
        if (!filterParameters.get(LOCATION).isEmpty()) {
            predicates.add(cb.equal(pigeonRoot.get(LOCATION), filterParameters.get(LOCATION)));
        }
        if (!filterParameters.get(PIGEON_NAME).isEmpty()) {
            predicates.add(cb.equal(pigeonRoot.get(PIGEON_NAME), filterParameters.get(PIGEON_NAME)));
        }
        TypedQuery<Pigeon> executableQuery = em.createQuery(cq);

        return executableQuery.getResultList();
    }
}
