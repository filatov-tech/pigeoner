package tech.filatov.pigeoner.repository;

import tech.filatov.pigeoner.dto.FilterParams;
import tech.filatov.pigeoner.dto.PigeonTableDto;

import java.util.List;

public interface PigeonRepositoryCustom {

    List<PigeonTableDto> getFiltered(FilterParams params);
}
