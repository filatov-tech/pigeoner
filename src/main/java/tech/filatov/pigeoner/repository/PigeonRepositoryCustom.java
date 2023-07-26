package tech.filatov.pigeoner.repository;

import tech.filatov.pigeoner.dto.FilterParams;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

import java.util.List;
import java.util.Map;

public interface PigeonRepositoryCustom {

    List<Pigeon> getFiltered(FilterParams params);
}
