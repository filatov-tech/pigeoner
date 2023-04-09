package tech.filatov.pigeoner.repository;

import tech.filatov.pigeoner.model.pigeon.Pigeon;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PigeonRepositoryCustom {

    List<Pigeon> getFiltered(Map<String, String> filterParameters);
}
