package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.HasId;
import tech.filatov.pigeoner.model.flight.Flight;
import tech.filatov.pigeoner.model.flight.FlightResult;
import tech.filatov.pigeoner.model.flight.FlightType;
import tech.filatov.pigeoner.model.flight.LaunchPoint;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonUtil {
    private CommonUtil() {}

    public static <T extends HasId> Map<Long, T> getLookupMapFrom(List<T> elements) {
        return elements.stream().collect(Collectors.toMap(T::getId, element -> element, (first, second) -> first));
    }

    public static String toCamelCaseAndCutUnderscoreId(String input) {
        String[] array = input.split("_");
        StringBuilder result = new StringBuilder(array[0].toLowerCase());
        for (int i = 1; i < array.length; i++) {
            if (array[i].equals("id")) continue;
            result.append(capitalizeFirstLetter(array[i]));
        }
        return result.toString();
    }

    public static Double calculateAvgSpeed(FlightResult flightResult) {
        checkFlightResult(flightResult);
        Flight flightData = flightResult.getFlight();

        double distanceInMeters = flightResult.getPreciseDistance() == null
                ? flightData.getLaunchPoint().getDistance() * 1000
                : flightResult.getPreciseDistance() * 1000;
        LocalDateTime start = flightData.getDeparture();
        LocalDateTime finish = flightResult.getArrivalTime();

        double flightDurationInSec = ((Long) ChronoUnit.SECONDS.between(start, finish)).doubleValue();
        double avgSpeedInMetersMin = distanceInMeters / (flightDurationInSec / 60);
        return new BigDecimal(avgSpeedInMetersMin).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static void checkFlightResult(FlightResult flightResult) {
        Flight flight = flightResult.getFlight();
        if (flight == null || flightResult.getArrivalTime() == null) {
            throw new IllegalStateException(
                    "Невозможно расчитать среднюю скорость участника без данных о времени старта и финиша"
            );
        }
        LaunchPoint launchPoint = flight.getLaunchPoint();
        if (launchPoint == null) {
            throw new IllegalStateException(
                    "Невозможно расчитать среднюю скорость участника без данных о дистанции"
            );
        }
        if (flightResult.getPreciseDistance() == null && flight.getFlightType() != FlightType.TRAINING) {
            throw new IllegalStateException(
                    "Невозможно расчитать среднюю скорость участника официального вылета без установленной точной дистаниции"
            );
        }
    }

    private static String capitalizeFirstLetter(String word) {
        StringBuilder result = new StringBuilder(word.length());
        result.append(Character.toUpperCase(word.charAt(0)));
        result.append(word.substring(1));
        return result.toString();
    }
}
