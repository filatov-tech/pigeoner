package tech.filatov.pigeoner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.PigeonLabelDto;

import tech.filatov.pigeoner.dto.PigeonDto;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

import java.util.List;

public interface PigeonRepository extends JpaRepository<Pigeon, Long>, PigeonRepositoryCustom {

    @Query("SELECT new tech.filatov.pigeoner.dto.PigeonLabelDto(p.id, p.ringNumber, p.sex, p.section.id) " +
            "FROM Pigeon p " +
            "WHERE p.owner.id = :userId AND p.isOwn IS TRUE")
    List<PigeonLabelDto> getAllLabelDto(long userId);

    @Query(nativeQuery = true)
    List<PigeonDto> getWithAncestorsById(long id);
}
