package tech.filatov.pigeoner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.PigeonLabelDto;
import tech.filatov.pigeoner.dto.PigeonWithAncestorsDto;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

import java.util.List;

public interface PigeonRepository extends JpaRepository<Pigeon, Long>, PigeonRepositoryCustom {

    @Query("SELECT p FROM Pigeon p ORDER BY p.ringNumber")
    List<Pigeon> getAll();

    @Query("SELECT p FROM Pigeon p WHERE p.section.id = :sectionId ORDER BY p.ringNumber")
    List<Pigeon> getAllByLocation(long sectionId);

    @Query("SELECT new tech.filatov.pigeoner.dto.PigeonLabelDto(p.id, p.ringNumber, p.sex, p.section.id) FROM Pigeon p")
    List<PigeonLabelDto> getAllLabelDto();

    @Query(nativeQuery = true)
    List<PigeonWithAncestorsDto> getWithAncestorsById(long id);
}
