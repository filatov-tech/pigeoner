package tech.filatov.pigeoner.repository.pigeon;

import tech.filatov.pigeoner.dto.FilterParams;
import tech.filatov.pigeoner.dto.PigeonShallowDto;

import java.util.List;

public interface PigeonRepositoryCustom {

    List<PigeonShallowDto> getFiltered(FilterParams params, long userId);
}
