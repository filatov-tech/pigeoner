package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.HasId;
import tech.filatov.pigeoner.dto.FilterParams;
import tech.filatov.pigeoner.util.exception.FilterContradictionException;
import tech.filatov.pigeoner.util.exception.IllegalRequestDataException;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;

import static tech.filatov.pigeoner.constant.Constants.*;

public class ValidationUtil {

    private ValidationUtil() {}

    public static void validateDataFromFilter(FilterParams params) {
        validateFilter(params);

        String dateFilterType = params.getDateFilterType();
        if (dateFilterType != null && !dateFilterType.isEmpty()) {
            switch (dateFilterType) {
                case (AGE_TYPE) -> validateAgeConstraint(params);
                case (BIRTHDATE_TYPE) -> validateBirthdateTypeConstraint(params);
            }
        }
    }

    public static void assureIdConsistent(HasId bean, long id) {
        if (bean.getId() == null) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalRequestDataException("ID обновляемой сущности должен быть равен: " + id);
        }
    }

    public static void checkNew(HasId bean) {
        if (bean.getId() != null) {
            throw new IllegalRequestDataException("ID создаваемой сущности должен быть равен null");
        }
    }

    private static void validateBirthdateTypeConstraint(FilterParams params) {
        //TODO: write date check
    }

    private static void validateFilter(FilterParams params) {
        boolean hasBirthdateFilter =
                params.getBirthdateFrom() != null
                        || params.getBirthdateTo() != null;

        boolean hasAgeFilter =
                params.getAgeYearFrom() != null
                        || params.getAgeMonthFrom() != null
                        || params.getAgeYearTo() != null
                        || params.getAgeMonthTo() != null;

        if (hasBirthdateFilter && hasAgeFilter) {
            throw new FilterContradictionException("Можно фильтровать либо по дате рождения, либо по возрасту");
        }
    }

    private static void validateAgeConstraint(FilterParams params) {
        int yearFrom = params.getAgeYearFrom() != null ? params.getAgeYearFrom() : 0;
        int monthFrom = params.getAgeMonthFrom() != null ? params.getAgeMonthFrom() + 1 : 0;
        int yearTo = params.getAgeYearTo() != null ? params.getAgeYearTo() : 0;
        int monthTo = params.getAgeMonthTo() != null ? params.getAgeMonthTo() + 1 : 0;

        LocalDate now = LocalDate.now();
        TemporalAmount yearFromMinus = Period.ofYears(yearFrom);
        TemporalAmount monthsFromMinus = Period.ofMonths(monthFrom);
        LocalDate from = now.minus(yearFromMinus).minus(monthsFromMinus);
        TemporalAmount yearToMinus = Period.ofYears(yearTo);
        TemporalAmount monthsToMinus = Period.ofMonths(monthTo);
        LocalDate to = now.minus(yearToMinus).minus(monthsToMinus);

        if (!from.isEqual(now) && !to.isEqual(now)) {
            if (from.isBefore(to)) {
                throw new FilterContradictionException("Возраст \"ОТ\" должен быть меньше, чем \"ДО\"");
            }
        }
    }
}
