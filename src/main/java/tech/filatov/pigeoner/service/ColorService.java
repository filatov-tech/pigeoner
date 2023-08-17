package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.dto.ColorDto;
import tech.filatov.pigeoner.model.pigeon.Color;
import tech.filatov.pigeoner.repository.pigeon.ColorRepository;
import tech.filatov.pigeoner.util.exception.NotFoundException;

import java.util.List;

import static tech.filatov.pigeoner.util.ValidationUtil.checkNotFoundWithId;

@Service
public class ColorService {
    private final ColorRepository repository;
    private final UserService userService;

    public ColorService(ColorRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    Color findOne(long id, long userId) {
        return repository.findByIdAndOwnerId(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public List<ColorDto> getAll(long userId) {
        return repository.getAllDtoByOwnerId(userId);
    }

    public ColorDto get(long id, long userId) {
        return repository.getDtoByIdAndOwnerId(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public Color getColorByName(String name, long userId) {
        return repository.getColorByNameAndOwnerId(name.toLowerCase(), userId)
                .orElseThrow(NotFoundException.withNameInfo(name));
    }

    public Color findColorByPigeonId(long id, long userId) {
        return repository.findColorByPigeonId(id, userId)
                .orElseThrow(NotFoundException.withInfo("У голубя не задан цвет"));
    }

    public ColorDto create(ColorDto colorDto, long userId) {
        Color color = new Color();
        color.setName(colorDto.getName().toLowerCase());
        color.setOwner(userService.get(userId));
        //noinspection ConstantConditions
        return get(repository.save(color).getId(), userId);
    }

    public ColorDto update(ColorDto colorDto, long id, long userId) {
        Color color = findOne(id, userId);
        color.setName(colorDto.getName());
        //noinspection ConstantConditions
        return get(repository.save(color).getId(), userId);
    }

    public void delete(long id, long userId) {
        checkNotFoundWithId(repository.deleteByIdAndOwnerId(id, userId) != 0, id);
    }
}
