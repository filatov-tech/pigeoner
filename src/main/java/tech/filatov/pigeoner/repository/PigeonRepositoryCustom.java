package tech.filatov.pigeoner.repository;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestParam;
import tech.filatov.pigeoner.model.Pigeon;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PigeonRepositoryCustom {

    List<Pigeon> getFiltered(Map<String, String> filterParameters);
}
