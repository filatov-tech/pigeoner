package tech.filatov.pigeoner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.FlightTableDto;
import tech.filatov.pigeoner.model.flight.Flight;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT new tech.filatov.pigeoner.dto.FlightTableDto(" +
            "f.locationName, f.distance, count(fr), f.myPassed, f.totalParticipants, f.departure) " +
            "FROM FlightResult fr " +
            "JOIN Pigeon p ON fr.pigeon.id = p.id " +
            "JOIN Flight f ON fr.flight.id = f.id " +
            "WHERE p.isNative = true AND fr.owner.id = :id AND f.owner.id = :id AND p.owner.id = :id " +
            "GROUP BY f.locationName, f.distance, f.myPassed, f.totalParticipants, f.id, f.departure")
    List<FlightTableDto> getAllFlightTableDto(long id);
}
