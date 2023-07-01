package tech.filatov.pigeoner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.FlightDto;
import tech.filatov.pigeoner.model.flight.Flight;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT new tech.filatov.pigeoner.dto.FlightDto(" +
            "f.id, f.locationName, f.distance, count(fr), f.myPassed, f.totalParticipants, f.departure, f.passingThreshold) " +
            "FROM FlightResult fr " +
            "JOIN Pigeon p ON fr.pigeon.id = p.id " +
            "JOIN Flight f ON fr.flight.id = f.id " +
            "WHERE p.isNative = true AND fr.owner.id = :userId AND f.owner.id = :userId AND p.owner.id = :userId " +
            "GROUP BY f.id, f.locationName, f.distance, f.myPassed, f.totalParticipants, f.id, f.departure, f.passingThreshold")
    List<FlightDto> getAllFlightDto(long userId);

    @Query("SELECT new tech.filatov.pigeoner.dto.FlightDto(" +
            "f.id, f.locationName, f.distance, count(fr), f.myPassed, f.totalParticipants, f.departure, f.passingThreshold) " +
            "FROM FlightResult fr " +
            "JOIN Pigeon p ON fr.pigeon.id = p.id " +
            "JOIN Flight f ON fr.flight.id = f.id " +
            "WHERE p.isNative = true AND f.id = :id AND fr.owner.id = :userId AND f.owner.id = :userId AND p.owner.id = :userId " +
            "GROUP BY f.id, f.locationName, f.distance, f.myPassed, f.totalParticipants, f.id, f.departure, f.passingThreshold")
    FlightDto getFlightDtoById(long id, long userId);

}
