package tech.filatov.pigeoner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.SectionDto;
import tech.filatov.pigeoner.model.dovecote.Section;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query("SELECT s FROM Section s WHERE s.parent IS NULL")
    List<Section> getTopLevel();

    @Query(nativeQuery = true)
    List<SectionDto> getTopLevelCommonSectionInfo();

    @Query(nativeQuery = true)
    List<SectionDto> getCommonSectionInfoById(int id);
}
