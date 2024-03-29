package tech.filatov.pigeoner.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import tech.filatov.pigeoner.dto.*;
import tech.filatov.pigeoner.model.Keeper;
import tech.filatov.pigeoner.model.User;
import tech.filatov.pigeoner.model.dovecote.Section;
import tech.filatov.pigeoner.model.dovecote.SectionType;
import tech.filatov.pigeoner.model.pigeon.Image;
import tech.filatov.pigeoner.model.pigeon.Pigeon;
import tech.filatov.pigeoner.model.pigeon.Sex;
import tech.filatov.pigeoner.repository.pigeon.PigeonRepository;
import tech.filatov.pigeoner.util.CommonUtil;
import tech.filatov.pigeoner.util.PigeonUtil;
import tech.filatov.pigeoner.util.exception.NotFoundException;
import tech.filatov.pigeoner.util.exception.NotPassValidationException;
import tech.filatov.pigeoner.validator.PigeonValidator;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static tech.filatov.pigeoner.util.PigeonUtil.fillWithUpdatedFields;
import static tech.filatov.pigeoner.util.PigeonUtil.getPigeonFrom;
import static tech.filatov.pigeoner.util.ValidationUtil.checkNotFoundWithId;

@Service
public class PigeonService {

    private final UserService userService;
    private final SectionService sectionService;
    private final PigeonRepository repository;
    private final ColorService colorService;
    private final ImageService imageService;
    private final PigeonValidator validator;
    private final KeeperService keeperService;
    private final FlightResultService flightResultService;

    public PigeonService(UserService userService, SectionService sectionService, PigeonRepository repository, ColorService colorService, ImageService imageService, PigeonValidator validator, KeeperService keeperService, @Lazy FlightResultService flightResultService) {
        this.userService = userService;
        this.sectionService = sectionService;
        this.repository = repository;
        this.colorService = colorService;
        this.imageService = imageService;
        this.validator = validator;
        this.keeperService = keeperService;
        this.flightResultService = flightResultService;
    }

    public Pigeon get(long id, long userId) {
        return repository.findByIdAndOwnerId(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public List<PigeonShallowDto> getAll(long userId)  {
        List<PigeonShallowDto> pigeons = repository.getAllPigeonShallowDto(userId);
        return addSectionsTo(pigeons, userId);
    }

    public List<PigeonShallowDto> getAllFiltered(FilterParams params, long userId) {
        List<PigeonShallowDto> pigeons = repository.getFiltered(params, userId);
        return addSectionsTo(pigeons, userId);
    }

    private List<PigeonShallowDto> addSectionsTo(List<PigeonShallowDto> pigeons, long userId) {
        Map<Long, SectionDto> sections = CommonUtil.getLookupMapFrom(
                sectionService.getAllWithFullAddress(userId)
        );
        for (PigeonShallowDto pigeon : pigeons) {
            if (pigeon.getSectionId() == null) continue;
            pigeon.setSection(sections.get(pigeon.getSectionId()));
        }
        return pigeons;
    }

    public PigeonDto getPigeonDtoWithoutNestedDto(long id, long userId) {
        return repository.findOneDto(id, userId).orElseThrow(NotFoundException.withIdInfo(id));
    }

    public PigeonDto getPigeonDto(long id, long userId) {
        PigeonDto pigeon = getPigeonDtoWithoutNestedDto(id, userId);
        return makeFull(pigeon, userId);
    }

    public PigeonDto getWithAncestors(long id, long userId) {
        PigeonDto pigeon = getPigeonDto(id, userId);

        List<PigeonDto> pigeons = repository.getWithAncestorsById(id, userId);
        PigeonDto pigeonWithAncestors = PigeonUtil.buildPedigree(pigeons, id);

        pigeon.setMother(pigeonWithAncestors.getMother());
        pigeon.setFather(pigeonWithAncestors.getFather());
        return pigeon;
    }

    public List<PigeonLabelDto> getAllLabelDto(long userId) {
        return repository.getAllLabelDto(userId);
    }

    public List<PigeonLabelDto> getLabelDtoBySectionId(long sectionId, long userId) {
        return repository.getLabelDtoBySectionId(sectionId, userId);
    }

    public List<Pigeon> getAllBySectionId(long sectionId, long userId) {
        return repository.getAllBySectionIdAndOwnerId(sectionId, userId);
    }

    public List<Pigeon> getAllBySectionsIds(List<Long> sectionIds, long userId) {
        return repository.getAllBySectionIdInAndOwnerId(sectionIds, userId);
    }

    @Transactional
    public PigeonDto create(PigeonShallowDto pigeonShallowDto, @Nullable MultipartFile[] images, long userId) {
        Pigeon pigeon = getPigeonFrom(pigeonShallowDto);
        initializeFullStateFrom(pigeonShallowDto, pigeon, userId);

        if (images != null) {
            return saveWithImage(pigeon, images, pigeonShallowDto.getMainImageFileName(), userId);
        }
        validate(pigeon);
        pigeon = save(pigeon);
        return getPigeonDto(Objects.requireNonNull(pigeon.getId()), userId);
    }

    @Transactional
    public PigeonDto update(PigeonShallowDto pigeonShallowDto,
                            @Nullable MultipartFile[] images,
                            long id,
                            long userId
    ) {
        Pigeon pigeon = fillWithUpdatedFields(get(id, userId), pigeonShallowDto);
        initializeFullStateFrom(pigeonShallowDto, pigeon, userId);

        if (images != null) {
            return saveWithImage(pigeon, images, pigeonShallowDto.getMainImageFileName(), userId);
        }

        validate(pigeonShallowDto, pigeon, userId);
        if (pigeonShallowDto.getFatherId() == null) {
            pigeon.setFather(null);
        } else {
            pigeon.setFather(get(pigeonShallowDto.getFatherId(), userId));
        }
        if (pigeonShallowDto.getMotherId() == null) {
            pigeon.setMother(null);
        } else {
            pigeon.setMother(get(pigeonShallowDto.getMotherId(), userId));
        }
        validate(pigeon);

        pigeon = save(pigeon);
        return getPigeonDto(Objects.requireNonNull(pigeon.getId()), userId);
    }

    @Transactional
    public void changeSection(long id, long newSectionId, long userId) {
        Pigeon pigeon = get(id, userId);
        if (newSectionId == -1L) {
            pigeon.setSection(null);
        } else {
            Section section = sectionService.findOne(newSectionId, userId);
            pigeon.setSection(section);
        }
        validate(pigeon);
        repository.save(pigeon);
    }

    @Transactional
    public void delete(long id, long userId) {
        Pigeon pigeon = get(id, userId);
        Set<Pigeon> children = repository.getAllDirectChildren(id, userId);
        for (Pigeon child : children) {
            if (pigeon.getSex() == Sex.MALE) {
                child.setFather(null);
            } else {
                child.setMother(null);
            }
        }
        repository.saveAllAndFlush(children);
        imageService.deleteAll(
                pigeon.getImages().stream().map(Image::getFileName).toList(),
                userId,
                id);
        repository.delete(pigeon);
    }

    protected Pigeon save(Pigeon pigeon) {
        return repository.save(pigeon);
    }

    public void saveAll(Iterable<Pigeon> pigeons) {
        repository.saveAllAndFlush(pigeons);
    }

    private PigeonDto saveWithImage(Pigeon pigeon,
                                    MultipartFile[] images,
                                    @Nullable String mainImageFileName,
                                    long userId) {
        Pigeon finalPigeon = pigeon;
        Set<Image> imagesForSave = Arrays.stream(images)
                .map(image -> new Image(
                        finalPigeon,
                        image.getOriginalFilename(),
                        Objects.requireNonNull(image.getOriginalFilename()).equalsIgnoreCase(mainImageFileName)))
                .collect(Collectors.toSet());
        if (pigeon.getId() != null) {
            removeNotIncludedImages(
                    pigeon.getImages(),
                    imagesForSave,
                    pigeon.getId(),
                    userId);
        }
        pigeon.setImages(imagesForSave);

        validate(pigeon);
        pigeon = save(pigeon);
        long createdPigeonId = Objects.requireNonNull(pigeon.getId());
        imageService.store(images, userId, createdPigeonId);

        return getPigeonDto(createdPigeonId, userId);
    }

    private PigeonDto makeFull(PigeonDto pigeon, long userId) {
        if (pigeon.getSectionId() != null) {
            SectionDto section = sectionService.getDtoWithFullAddress(pigeon.getSectionId(), userId);
            pigeon.setSection(section);
        }
        if (pigeon.getMateId() != null) {
            pigeon.setMate(getPigeonDtoWithoutNestedDto(pigeon.getMateId(), userId));
        }
        if (pigeon.getFatherId() != null) {
            pigeon.setFather(getPigeonDtoWithoutNestedDto(pigeon.getFatherId(), userId));
        }
        if (pigeon.getMotherId() != null) {
            pigeon.setMother(getPigeonDtoWithoutNestedDto(pigeon.getMotherId(), userId));
        }
        if (pigeon.getColorId() != null) {
            pigeon.setColor(colorService.get(pigeon.getColorId(), userId).getName());
        }
        List<FlightResultDto> flightResults = flightResultService.getAllDtoByPigeonId(pigeon.getId(), userId);
        if (!flightResults.isEmpty()) {
            pigeon.setFlights(flightResults);
        }
        pigeon.setImageNumber(repository.getImagesNumber(pigeon.getId(), userId));
        return pigeon;
    }

    private void removeNotIncludedImages(Set<Image> previousImages, Set<Image> imagesForSave, long id, long userId) {
        previousImages.removeAll(imagesForSave);
        for (Image imageForDelete : previousImages) {
            imageService.delete(imageForDelete.getFileName(), userId, id);
        }
    }

    private void validate(PigeonShallowDto source, Pigeon pigeon, long userId) {
        Errors errors = new BeanPropertyBindingResult(pigeon, "pigeon");
        if (pigeon.isNew()) {
            validator.validate(pigeon, errors);
        } else {
            List<PigeonDto> descendants = repository.getWithAllDescendantsById(Objects.requireNonNull(pigeon.getId()), userId);
            validator.validate(
                    pigeon,
                    errors,
                    CommonUtil.getLookupMapFrom(descendants),
                    source.getFatherId(),
                    source.getMotherId()
                    );
        }
        if (errors.hasErrors()) {
            throw new NotPassValidationException(errors);
        }
    }

    private void validate(Pigeon pigeon) {
        Errors errors = new BeanPropertyBindingResult(pigeon, "pigeon");
        validator.validate(pigeon, errors);
        if (errors.hasErrors()) {
            throw new NotPassValidationException(errors);
        }
    }

    private void initializeFullStateFrom(PigeonShallowDto source, Pigeon pigeon, long userId) {
        pigeon.setOwner(userService.get(userId));
        if (source.getMateId() != null) {
            pigeon.setMate(get(source.getMateId(), userId));
        }
        if (source.getFatherId() != null && pigeon.isNew()) {
            pigeon.setFather(get(source.getFatherId(), userId));
        }
        if (source.getMotherId() != null && pigeon.isNew()) {
            pigeon.setMother(get(source.getMotherId(), userId));
        }
        if (source.getSectionId() != null) {
            Section section = sectionService.findOne(source.getSectionId(), userId);
            if (section.getType() == SectionType.NEST) {
                section = sectionService.getWithPigeons(source.getSectionId(), userId);
            }
            pigeon.setSection(section);
        } else if (!pigeon.isNew()) {
            pigeon.setSection(null);
        }
        if (source.getKeeperId() != null) {
            pigeon.setKeeper(keeperService.get(source.getKeeperId(), userId));
        }
        if (source.getColor() != null) {
            pigeon.setColor(colorService.getColorByName(source.getColor(), userId));
        }
        User owner = pigeon.getOwner() != null ? pigeon.getOwner() : null;
        Keeper mainKeeper = owner != null ? owner.getKeeper() : null;
        Keeper pigeonKeeper = pigeon.getKeeper();
        if (pigeonKeeper != null && pigeonKeeper.equals(mainKeeper)) {
            pigeon.setOwn(true);
        }
    }
}
