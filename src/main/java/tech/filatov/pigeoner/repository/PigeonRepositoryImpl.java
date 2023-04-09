package tech.filatov.pigeoner.repository;

import org.springframework.stereotype.Repository;
import tech.filatov.pigeoner.model.pigeon.Condition;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.*;

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
        /*
         *  TODO: отрефакторить эту простыню
         *   вариант - создать объект, который будет вначале сконфигурирован билдером имеющимися
         *   параметрами, такими как CriteriaBuilder, списком фильтров, Root-объектом, списком предикат,
         *   а затем вернет составленный верно список предикат.
         *   То есть, вся работа по наполнению запроса динамическими параметрами уйдет в отдельный объект и не будет
         *   засорять здесь метод
         */


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

        String filterDateType = filterParameters.get(FILTER_DATE_TYPE);
        if (!filterDateType.isEmpty()) {
            if (filterDateType.equals(BIRTHDATE_TYPE)) {
                if (!filterParameters.get(BIRTHDATE_FROM).isEmpty()) {
                    predicates.add(
                            cb.greaterThanOrEqualTo(pigeonRoot.get("birthdate"), LocalDate.parse(filterParameters.get(BIRTHDATE_FROM)))
                    );
                }
                if (!filterParameters.get(BIRTHDATE_TO).isEmpty()) {
                    predicates.add(
                            cb.lessThanOrEqualTo(pigeonRoot.get("birthdate"), LocalDate.parse(filterParameters.get(BIRTHDATE_TO)))
                    );
                }
            } else if (filterDateType.equals(AGE_TYPE)) {
                LocalDate now = LocalDate.now();

                LocalDate from = LocalDate.now();
                if (!filterParameters.get(AGE_YEAR_FROM).isEmpty() || !filterParameters.get(AGE_MONTH_FROM).isEmpty()) {
                    int yearsFrom = 0;
                    int monthsFrom = 0;
                    try {
                        yearsFrom = Integer.parseInt(filterParameters.get(AGE_YEAR_FROM));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    try {
                        monthsFrom = Integer.parseInt(filterParameters.get(AGE_MONTH_FROM));
                        int monthInclusive = 1;
                        monthsFrom -= monthInclusive;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    TemporalAmount yearsMinus = Period.ofYears(yearsFrom);
                    TemporalAmount monthsMinus = Period.ofMonths(monthsFrom);
                    from = now.minus(yearsMinus).minus(monthsMinus);
                }

                LocalDate to = LocalDate.now();
                if (!filterParameters.get(AGE_YEAR_TO).isEmpty() || !filterParameters.get(AGE_MONTH_TO).isEmpty()) {
                    int yearsTo = 0;
                    int monthsTo = 0;
                    try {
                        yearsTo = Integer.parseInt(filterParameters.get(AGE_YEAR_TO));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    try {
                        monthsTo = Integer.parseInt(filterParameters.get(AGE_MONTH_TO));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    TemporalAmount yearsMin = Period.ofYears(yearsTo);
                    TemporalAmount monthsMin = Period.ofMonths(monthsTo);
                    to = now.minus(yearsMin).minus(monthsMin);
                }
                if (!from.isEqual(now)) {
                    predicates.add(cb.lessThanOrEqualTo(pigeonRoot.get("birthdate"), from));
                }
                if (!to.isEqual(now)) {
                    predicates.add(cb.greaterThanOrEqualTo(pigeonRoot.get("birthdate"), to));
                }
            }
        }

        cq.where(predicates.toArray(new Predicate[predicates.size()]));

        TypedQuery<Pigeon> executableQuery = em.createQuery(cq);

        return executableQuery.getResultList();
    }
}
