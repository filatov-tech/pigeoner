package tech.filatov.pigeoner.repository;

import org.springframework.stereotype.Repository;
import tech.filatov.pigeoner.dto.FilterParams;
import tech.filatov.pigeoner.dto.PigeonTableDto;
import tech.filatov.pigeoner.model.Keeper;
import tech.filatov.pigeoner.model.dovecote.Section;
import tech.filatov.pigeoner.model.dovecote.Section_;
import tech.filatov.pigeoner.model.pigeon.Color;
import tech.filatov.pigeoner.model.pigeon.Color_;
import tech.filatov.pigeoner.model.pigeon.Pigeon;
import tech.filatov.pigeoner.model.pigeon.Pigeon_;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.*;

import static tech.filatov.pigeoner.constant.Constants.*;
import static tech.filatov.pigeoner.util.SectionUtil.*;

@Repository
public class PigeonRepositoryImpl implements PigeonRepositoryCustom {

    EntityManager em;
    private final SectionRepository sectionRepository;

    public PigeonRepositoryImpl(EntityManager em, SectionRepository sectionRepository) {
        this.em = em;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public List<PigeonTableDto> getFiltered(FilterParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PigeonTableDto> cq = cb.createQuery(PigeonTableDto.class);

        Root<Pigeon> pigeonRoot = cq.from(Pigeon.class);
        Join<Pigeon, Section> section = pigeonRoot.join(Pigeon_.section, JoinType.LEFT);
        Join<Pigeon, Pigeon> mate = pigeonRoot.join(Pigeon_.mate, JoinType.LEFT);
        Join<Pigeon, Color> color = pigeonRoot.join(Pigeon_.color, JoinType.LEFT);

        cq.select(cb.construct(
                PigeonTableDto.class,
                pigeonRoot.get(Pigeon_.id),
                pigeonRoot.get(Pigeon_.ringNumber),
                color.get(Color_.name),
                pigeonRoot.get(Pigeon_.sex),
                pigeonRoot.get(Pigeon_.birthdate),
                mate.get(Pigeon_.ringNumber),
                pigeonRoot.get(Pigeon_.conditionStatus),
                section.get(Section_.id)
        ));

        cq.where(preparePredicates(params, cb, pigeonRoot));

        TypedQuery<PigeonTableDto> executableQuery = em.createQuery(cq);

        return executableQuery.getResultList();
    }

    private Predicate[] preparePredicates(FilterParams params, CriteriaBuilder cb, Root<Pigeon> pigeonRoot) {
        List<Predicate> predicates = new ArrayList<>();

        //TODO добавить в запрос обязательное условия соответсвия owner по userId
        if (params.getRingNumber() != null) {
            predicates.add(cb.equal(pigeonRoot.get(Pigeon_.ringNumber), params.getRingNumber()));
        }
        if (params.getCondition() != null) {
            predicates.add(
                    cb.equal(
                            pigeonRoot.get(Pigeon_.conditionStatus),
                            params.getCondition()
                    )
            );
        }
        if (params.getDovecote() != null) {
            long currentSectionId = params.getDovecote();
            List<Long> idList = sectionRepository.getIdListOfAllDescendantsById(currentSectionId);
            CriteriaBuilder.In<Section> inSections = cb.in(pigeonRoot.get(Pigeon_.section));
            for (Section section : makeSectionsFrom(idList)) {
                inSections.value(section);
            }
            predicates.add(inSections);
        }
        if (params.getName() != null) {
            predicates.add(cb.equal(pigeonRoot.get(Pigeon_.name), params.getName()));
        }

        String filterDateType = params.getDateFilterType();
        if (filterDateType != null && !filterDateType.isEmpty()) {
            if (filterDateType.equals(BIRTHDATE_TYPE)) {
                if (params.getBirthdateFrom() != null) {
                    predicates.add(
                            cb.greaterThanOrEqualTo(pigeonRoot.get(Pigeon_.birthdate), params.getBirthdateFrom())
                    );
                }
                if (params.getBirthdateTo() != null) {
                    predicates.add(
                            cb.lessThanOrEqualTo(pigeonRoot.get(Pigeon_.birthdate), params.getBirthdateTo())
                    );
                }
            } else if (filterDateType.equals(AGE_TYPE)) {
                LocalDate now = LocalDate.now();

                LocalDate from = LocalDate.now();
                if (params.getAgeYearFrom() != null || params.getAgeMonthFrom() != null) {
                    int yearsFrom = 0;
                    int monthsFrom = 0;
                    yearsFrom = params.getAgeYearFrom();
                    monthsFrom = params.getAgeMonthFrom();
                    int monthInclusive = 1;
                    monthsFrom -= monthInclusive;
                    TemporalAmount yearsMinus = Period.ofYears(yearsFrom);
                    TemporalAmount monthsMinus = Period.ofMonths(monthsFrom);
                    from = now.minus(yearsMinus).minus(monthsMinus);
                }

                LocalDate to = LocalDate.now();
                if (params.getAgeYearTo() != null || params.getAgeMonthTo() != null) {
                    int yearsTo = 0;
                    int monthsTo = 0;
                    yearsTo = params.getAgeYearTo();
                    monthsTo = params.getAgeMonthTo();
                    TemporalAmount yearsMin = Period.ofYears(yearsTo);
                    TemporalAmount monthsMin = Period.ofMonths(monthsTo);
                    to = now.minus(yearsMin).minus(monthsMin);
                }
                if (!from.isEqual(now)) {
                    predicates.add(cb.lessThanOrEqualTo(pigeonRoot.get(Pigeon_.birthdate), from));
                }
                if (!to.isEqual(now)) {
                    predicates.add(cb.greaterThanOrEqualTo(pigeonRoot.get(Pigeon_.birthdate), to));
                }
            }
        }
        if (params.getSex() != null) {
            predicates.add(cb.equal(pigeonRoot.get(Pigeon_.sex), params.getSex()));
        }

        if (params.getHasMate() != null) {
            if (params.getHasMate()) {
                predicates.add(cb.notEqual(pigeonRoot.get(Pigeon_.mate), null));
            } else {
                predicates.add(cb.equal(pigeonRoot.get(Pigeon_.mate), null));
            }

        }

        if (params.getKeeper() != null) {
            predicates.add(cb.equal(pigeonRoot.get(Pigeon_.keeper), new Keeper(params.getKeeper())));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }
}
