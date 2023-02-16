package tech.filatov.pigeoner.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TimeUtil {
    private TimeUtil () {}

    public static String getAgeFromBirthday(LocalDate birthday) {
        long ageInMonths = ChronoUnit.MONTHS.between(birthday, LocalDate.now());
        long fullYears = ageInMonths / 12;
        long months = ageInMonths % 12;
        String yearsSymbol = fullYears < 5 ? "г" : "л";
        return fullYears + yearsSymbol + " " + months + "м";
    }
}
