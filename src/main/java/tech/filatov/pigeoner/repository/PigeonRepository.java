package tech.filatov.pigeoner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.PigeonLabelDto;
import tech.filatov.pigeoner.dto.PigeonTableDto;
import tech.filatov.pigeoner.dto.PigeonWithAncestorsDto;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

import java.util.List;

public interface PigeonRepository extends JpaRepository<Pigeon, Long>, PigeonRepositoryCustom {

    @Query("SELECT new tech.filatov.pigeoner.dto.PigeonTableDto(" +
            "p.id, p.ringNumber, c.name, p.sex, p.birthdate, m.id, m.ringNumber, p.conditionStatus, s.id, s.name) " +
            "FROM Pigeon p " +
            "LEFT OUTER JOIN Pigeon m ON p.mate.id = m.id " +
            "LEFT OUTER JOIN Section s ON p.section.id = s.id " +
            "LEFT OUTER JOIN Color c ON p.color.id = c.id " +
            "WHERE p.owner.id = :userId " +
            "ORDER BY p.ringNumber")
    List<PigeonTableDto> getAll(long userId);

    @Query("SELECT p FROM Pigeon p WHERE p.section.id = :sectionId ORDER BY p.ringNumber")
    List<Pigeon> getAllByLocation(long sectionId);

    @Query("SELECT new tech.filatov.pigeoner.dto.PigeonLabelDto(p.id, p.ringNumber, p.sex, p.section.id) FROM Pigeon p")
    List<PigeonLabelDto> getAllLabelDto();

    @Query(nativeQuery = true)
    List<PigeonWithAncestorsDto> getWithAncestorsById(long id);
}
