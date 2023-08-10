package tech.filatov.pigeoner.service;

import tech.filatov.pigeoner.model.pigeon.Color;
import tech.filatov.pigeoner.repository.ColorRepository;
import tech.filatov.pigeoner.util.exception.NotFoundException;

public class ColorService {
    private final ColorRepository repository;

    public ColorService(ColorRepository repository) {
        this.repository = repository;
    }

    public Color getColorByName(String name, long userId) {
        return repository.getColorByNameAndOwnerId(name, userId)
                .orElseThrow(NotFoundException.supplier(name));
    }
}
