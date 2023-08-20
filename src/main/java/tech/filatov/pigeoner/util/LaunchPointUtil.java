package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.dto.LaunchPointDto;
import tech.filatov.pigeoner.model.flight.LaunchPoint;

public class LaunchPointUtil {

    private LaunchPointUtil() {}

    public static LaunchPoint makeInstanceFrom(LaunchPointDto dto) {
        LaunchPoint launchPoint = new LaunchPoint();
        return setDataTo(launchPoint, dto);
    }

    public static LaunchPoint fillWithUpdatedFields(LaunchPoint target, LaunchPointDto dto) {
        return setDataTo(target, dto);
    }

    private static LaunchPoint setDataTo(LaunchPoint target, LaunchPointDto dto) {
        target.setName(dto.getName());
        target.setDistance(dto.getDistance());
        return target;
    }

}
