package tech.filatov.pigeoner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.FlightDto;
import tech.filatov.pigeoner.model.flight.Flight;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT new tech.filatov.pigeoner.dto.FlightDto(" +
            "f.id, lp.name, lp.distance, count(fr), f.myPassed, f.totalParticipants, f.departure, f.passingThreshold, f.flightType) " +
            "FROM FlightResult fr " +
            "JOIN Pigeon p ON fr.pigeon.id = p.id " +
            "JOIN Flight f ON fr.flight.id = f.id " +
            "JOIN LaunchPoint lp ON f.launchPoint.id = lp.id " +
            "WHERE p.isNative = true AND fr.owner.id = :userId AND f.owner.id = :userId AND p.owner.id = :userId AND lp.owner.id = :userId " +
            "GROUP BY f.id, lp.name, lp.distance, f.myPassed, f.totalParticipants, f.id, f.departure, f.passingThreshold, f.flightType")
    List<FlightDto> getAllFlightDto(long userId);

    @Query("SELECT new tech.filatov.pigeoner.dto.FlightDto(" +
            "f.id, lp.name, lp.distance, count(fr), f.myPassed, f.totalParticipants, f.departure, f.passingThreshold, f.flightType) " +
            "FROM FlightResult fr " +
            "JOIN Pigeon p ON fr.pigeon.id = p.id " +
            "JOIN Flight f ON fr.flight.id = f.id " +
            "JOIN LaunchPoint lp ON f.launchPoint.id = lp.id " +
            "WHERE p.isNative = true AND f.id = :id AND fr.owner.id = :userId AND f.owner.id = :userId AND p.owner.id = :userId AND lp.owner.id = :userId " +
            "GROUP BY f.id, lp.name, lp.distance, f.myPassed, f.totalParticipants, f.id, f.departure, f.passingThreshold, f.flightType")
    FlightDto getFlightDtoById(long id, long userId);

}
