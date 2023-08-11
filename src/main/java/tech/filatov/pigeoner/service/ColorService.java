package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.model.pigeon.Color;
import tech.filatov.pigeoner.repository.ColorRepository;
import tech.filatov.pigeoner.util.exception.NotFoundException;

@Service
public class ColorService {
    private final ColorRepository repository;

    public ColorService(ColorRepository repository) {
        this.repository = repository;
    }

    public Color getColorByName(String name, long userId) {
        return repository.getColorByNameAndOwnerId(name, userId)
                .orElseThrow(NotFoundException.withNameInfo(name));
    }

    public Color findColorByPigeonId(long id, long userId) {
        return repository.findColorByPigeonId(id, userId);
    }
}
