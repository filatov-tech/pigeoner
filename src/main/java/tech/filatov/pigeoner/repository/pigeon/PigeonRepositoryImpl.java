package tech.filatov.pigeoner.repository.pigeon;

import org.springframework.stereotype.Repository;
import tech.filatov.pigeoner.dto.FilterParams;
import tech.filatov.pigeoner.dto.PigeonShallowDto;
import tech.filatov.pigeoner.model.Keeper;
import tech.filatov.pigeoner.model.Keeper_;
import tech.filatov.pigeoner.model.dovecote.Section;
import tech.filatov.pigeoner.model.dovecote.Section_;
import tech.filatov.pigeoner.model.pigeon.Color;
import tech.filatov.pigeoner.model.pigeon.Color_;
import tech.filatov.pigeoner.model.pigeon.Pigeon;
import tech.filatov.pigeoner.model.pigeon.Pigeon_;
import tech.filatov.pigeoner.repository.SectionRepository;
import tech.filatov.pigeoner.util.DateTimeUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static tech.filatov.pigeoner.util.SectionUtil.makeSectionsFrom;

@Repository
public class PigeonRepositoryImpl implements PigeonRepositoryCustom {

    EntityManager em;
    private final SectionRepository sectionRepository;

    public PigeonRepositoryImpl(EntityManager em, SectionRepository sectionRepository) {
        this.em = em;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public List<PigeonShallowDto> getFiltered(FilterParams params, long userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PigeonShallowDto> cq = cb.createQuery(PigeonShallowDto.class);

        Root<Pigeon> pigeonRoot = cq.from(Pigeon.class);
        Join<Pigeon, Pigeon> mate = pigeonRoot.join(Pigeon_.mate, JoinType.LEFT);
        Join<Pigeon, Color> color = pigeonRoot.join(Pigeon_.color, JoinType.LEFT);

        cq.select(cb.construct(
                PigeonShallowDto.class,
                pigeonRoot.get(Pigeon_.id),
                pigeonRoot.get(Pigeon_.name),
                pigeonRoot.get(Pigeon_.ringNumber),
                pigeonRoot.get(Pigeon_.birthdate),
                color.get(Color_.name),
                pigeonRoot.get(Pigeon_.conditionStatus),
                pigeonRoot.get(Pigeon_.sex),
                pigeonRoot.get(Pigeon_.isOwn),
                pigeonRoot.get(Pigeon_.countryCode),
                mate.get(Pigeon_.id),
                mate.get(Pigeon_.ringNumber),
                pigeonRoot.get(Pigeon_.father).get(Pigeon_.id),
                pigeonRoot.get(Pigeon_.mother).get(Pigeon_.id),
                pigeonRoot.get(Pigeon_.keeper).get(Keeper_.id),
                pigeonRoot.get(Pigeon_.section).get(Section_.id)
        ));

        if (params != null) {
            cq.where(preparePredicates(params, cb, pigeonRoot, userId));
        } else {
            cq.where(cb.equal(pigeonRoot.get(Pigeon_.owner), userId));
        }

        TypedQuery<PigeonShallowDto> executableQuery = em.createQuery(cq);
        return executableQuery.getResultList();
    }

    private Predicate[] preparePredicates(FilterParams params, CriteriaBuilder cb, Root<Pigeon> pigeonRoot, long userId) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(pigeonRoot.get(Pigeon_.owner), userId));

        if (params.getRingNumber() != null) {
            predicates.add(cb.like(pigeonRoot.get(Pigeon_.ringNumber), String.format("%%%s%%", params.getRingNumber())));
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
            List<Long> idList = sectionRepository.getIdListOfAllDescendantsById(currentSectionId, userId);
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
            LocalDate[] dateRange = DateTimeUtil.getDateRangeFrom(params);
            LocalDate from = dateRange[0];
            LocalDate to = dateRange[1];
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(pigeonRoot.get(Pigeon_.birthdate), from));
            }
            if (to != null) {
                predicates.add(cb.lessThan(pigeonRoot.get(Pigeon_.birthdate), to));
            }
        }

        if (params.getSex() != null) {
            predicates.add(cb.equal(pigeonRoot.get(Pigeon_.sex), params.getSex()));
        }

        if (params.getHasMate() != null) {
            if (params.getHasMate()) {
                predicates.add(cb.isNotNull(pigeonRoot.get(Pigeon_.mate)));
            } else {
                predicates.add(cb.isNull(pigeonRoot.get(Pigeon_.mate)));
            }

        }

        if (params.getKeeper() != null) {
            predicates.add(cb.equal(pigeonRoot.get(Pigeon_.keeper), new Keeper(params.getKeeper())));
        }

        if (params.getCountryCode() != null) {
            predicates.add(cb.equal(pigeonRoot.get(Pigeon_.countryCode), params.getCountryCode()));
        }

        return predicates.toArray(new Predicate[]{});
    }
}
