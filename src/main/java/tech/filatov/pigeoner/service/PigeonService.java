package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.dto.PigeonTableDto;
import tech.filatov.pigeoner.model.Pigeon;
import tech.filatov.pigeoner.repository.PigeonRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static tech.filatov.pigeoner.util.PigeonUtil.*;

@Service
public class PigeonService {

    private final PigeonRepository repository;

    public PigeonService(PigeonRepository repository) {
        this.repository = repository;
    }

    public List<PigeonTableDto> getAll(Map<String, String> filterParameters) {
        List<Pigeon> resultList = filterParameters.isEmpty() ?
                repository.getAll() :
                repository.getFiltered(filterParameters);
        return getDtos(resultList);
    }
}
