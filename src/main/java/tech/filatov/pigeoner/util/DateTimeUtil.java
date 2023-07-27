package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.dto.FilterParams;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

import static tech.filatov.pigeoner.constant.Constants.*;

public class DateTimeUtil {
    private DateTimeUtil() {}

    public static String getAgeFromDate(LocalDate birthday) {
        long ageInMonths = ChronoUnit.MONTHS.between(birthday, LocalDate.now());
        long fullYears = ageInMonths / 12;
        long months = ageInMonths % 12;
        String yearsSymbol = fullYears < 5 ? "г" : "л";
        return fullYears + yearsSymbol + " " + months + "м";
    }

    public static LocalDate[] getDateRangeFromAgeRange(@Nullable Integer yearsFrom,
                                                       @Nullable Integer monthsFrom,
                                                       @Nullable Integer yearsTo,
                                                       @Nullable Integer monthsTo) {
        LocalDate from = getDateFromYearAndMonthRemovedFromNow(yearsTo, monthsTo);
        LocalDate to = getDateFromYearAndMonthRemovedFromNow(yearsFrom, monthsFrom);
        return new LocalDate[]{from, to};
    }

    private static LocalDate getDateFromYearAndMonthRemovedFromNow(Integer year, Integer month) {
        TemporalAmount years = Period.ofYears(year == null ? 0 : year);
        TemporalAmount months = Period.ofMonths(month == null ? 0 : month);
        return LocalDate.now().minus(years).minus(months);
    }

    public static LocalDate[] getDateRangeFrom(FilterParams params) {
        LocalDate from = null;
        LocalDate to = null;
        switch (params.getDateFilterType()) {
            case BIRTHDATE_TYPE -> {
                from = params.getBirthdateFrom();
                to = params.getBirthdateTo();
            }
            case YEAR_TYPE -> {
                if (params.getYearFrom() != null) {
                    from = LocalDate.of(params.getYearFrom().getYear(), 1, 1);
                }
                if (params.getYearTo() != null) {
                    to = LocalDate.of(params.getYearTo().getYear(), 1, 1);
                }
            }
            case AGE_TYPE -> {
                LocalDate[] dateRange = DateTimeUtil.getDateRangeFromAgeRange(
                        params.getAgeYearFrom(),
                        params.getAgeMonthFrom(),
                        params.getAgeYearTo(),
                        params.getAgeMonthTo()
                );
                from = dateRange[0];
                to = dateRange[1];
            }
        }
        return new LocalDate[]{from, to};
    }
}
