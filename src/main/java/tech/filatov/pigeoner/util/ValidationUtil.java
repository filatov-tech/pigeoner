package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.util.exception.FilterContradictionException;
import tech.filatov.pigeoner.util.exception.InvalidFilterDataException;

import java.time.LocalDate;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tech.filatov.pigeoner.constant.Constants.*;

public class ValidationUtil {

    private ValidationUtil() {}

    public static void validateDataFromFilter(Map<String, String> filterParameters) {
        validateFilter(filterParameters);
        if (!filterParameters.get(FILTER_DATE_TYPE).isEmpty()) {
            validateAgeConstraint(filterParameters);
        }
        validateInputStringData(filterParameters);
    }

    private static void validateFilter(Map<String, String> filterParameters) {
        boolean hasBirthdateFilter =
                !filterParameters.get(BIRTHDATE_FROM).isEmpty()
                        || !filterParameters.get(BIRTHDATE_TO).isEmpty();

        boolean hasAgeFilter =
                !filterParameters.get(AGE_YEAR_FROM).isEmpty()
                        || !filterParameters.get(AGE_MONTH_FROM).isEmpty()
                        || !filterParameters.get(AGE_YEAR_TO).isEmpty()
                        || !filterParameters.get(AGE_MONTH_TO).isEmpty();

        if (hasBirthdateFilter && hasAgeFilter) {
            throw new FilterContradictionException("Можно фильтровать либо по дате рождения, либо по возрасту");
        }
        setFilterDateType(hasBirthdateFilter, hasAgeFilter, filterParameters);
    }

    private static void setFilterDateType(boolean hasBirthdateFilter,
                                          boolean hasAgeFilter,
                                          Map<String, String> filterParameters) {
        String filterDateType = "";
        if (hasBirthdateFilter) {
            filterDateType = BIRTHDATE_TYPE;
        } else if (hasAgeFilter) {
            filterDateType = AGE_TYPE;
        }
        filterParameters.put(FILTER_DATE_TYPE, filterDateType);
    }

    private static void validateAgeConstraint(Map<String, String> filterParameters) {


        int yearFrom = getNumberFromParameter(filterParameters.get(AGE_YEAR_FROM));
        int monthFrom = getNumberFromParameter(filterParameters.get(AGE_MONTH_FROM));
        int yearTo = getNumberFromParameter(filterParameters.get(AGE_YEAR_TO));
        int monthTo = getNumberFromParameter(filterParameters.get(AGE_MONTH_TO));

        LocalDate from = LocalDate.of(yearFrom, monthFrom, 1);

        LocalDate to;



        if (yearFrom > yearTo
                || yearFrom == yearTo && monthFrom >= monthTo) {
            throw new FilterContradictionException("Возраст \"ОТ\" должен быть меньше, чем \"ДО\"");
        }
    }

    private static void validateInputStringData(Map<String, String> filterParameters) {
        if (!filterParameters.get(RING_NUMBER).isEmpty()) {
            try {
                Integer.parseInt(filterParameters.get(RING_NUMBER));
            } catch (NumberFormatException e) {
                throw new InvalidFilterDataException("Номер кольца должен содержать только цифры");
            }
        }
        if (!filterParameters.get(PIGEON_NAME).isEmpty()) {
            checkPermittedSymbols(filterParameters.get(PIGEON_NAME));
        }
    }

    private static int getNumberFromParameter(String parameterWithNumber) {
        int number;
        try {
            number = Integer.parseInt(parameterWithNumber);
        } catch (NumberFormatException e) {
            number = 0;
        }
        return number;
    }

    private static void checkPermittedSymbols(String stringToCheck) {
        Pattern pattern = Pattern.compile("^[0-9А-Яа-я\\s]+\\Z");
        Matcher matcher = pattern.matcher(stringToCheck);
        if (!matcher.find()) {
            throw new InvalidFilterDataException("Строка может содержать только цифры и кириллические буквы");
        }
    }
}
