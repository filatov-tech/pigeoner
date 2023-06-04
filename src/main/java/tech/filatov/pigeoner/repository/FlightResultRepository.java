package tech.filatov.pigeoner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.FlightResultDto;
import tech.filatov.pigeoner.model.flight.FlightResult;

import java.util.List;

public interface FlightResultRepository extends JpaRepository<FlightResult, Long> {

    @Query("SELECT new tech.filatov.pigeoner.dto.FlightResultDto(" +
            "fr.pigeon.id, fl.locationName, fl.distance, fr.position, fl.totalParticipants, fr.arrivalTime, fr.isPass) " +
            "FROM FlightResult fr JOIN Flight fl ON fr.flight.id = fl.id " +
            "WHERE fr.pigeon.id = :id")
    List<FlightResultDto> getAllByPigeonId(long id);
}
