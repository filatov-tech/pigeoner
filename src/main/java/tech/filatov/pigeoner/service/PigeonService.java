package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.dto.PigeonTableDto;
import tech.filatov.pigeoner.repository.PigeonRepository;

import java.util.List;
import java.util.Map;

import static tech.filatov.pigeoner.util.PigeonUtil.*;

@Service
public class PigeonService {

    private final PigeonRepository repository;

    public PigeonService(PigeonRepository repository) {
        this.repository = repository;
    }

    public List<PigeonTableDto> getAll()  {
        return getDtos(repository.getAll());
    }

    public List<PigeonTableDto> getAll(Map<String, String> filterParameters) {
        return getDtos(repository.getFiltered(filterParameters));
    }
}
