package tech.filatov.pigeoner.repository.flight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.FlightResultDto;
import tech.filatov.pigeoner.model.flight.FlightResult;

import java.util.List;

public interface FlightResultRepository extends JpaRepository<FlightResult, Long> {

    @Query("SELECT new tech.filatov.pigeoner.dto.FlightResultDto(" +
            "fr.pigeon.id, lp.name, lp.distance, fr.position, fl.totalParticipants, fr.arrivalTime, fr.isPass) " +
            "FROM FlightResult fr " +
            "JOIN Flight fl ON fr.flight.id = fl.id " +
            "JOIN LaunchPoint lp ON fl.launchPoint.id = lp.id " +
            "WHERE fr.pigeon.id = :id AND fr.owner.id = :userId AND fl.owner.id = :userId AND lp.owner.id = :userId")
    List<FlightResultDto> getAllByPigeonId(long id, long userId);

    @Query("SELECT new tech.filatov.pigeoner.dto.FlightResultDto(" +
            "fr.id, p.id, p.ringNumber, fr.position, fr.arrivalTime, fr.isPass, fr.winPoints, fr.averageSpeed, fr.condition, p.keeper.name) " +
            "FROM FlightResult fr JOIN Pigeon p ON fr.pigeon.id = p.id " +
            "WHERE fr.flight.id = :id AND fr.owner.id = :userId AND p.owner.id = :userId")
    List<FlightResultDto> getAllByFlightId(long id, long userId);
}
