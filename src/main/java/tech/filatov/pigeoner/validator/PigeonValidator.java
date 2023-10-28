package tech.filatov.pigeoner.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import tech.filatov.pigeoner.dto.PigeonDto;
import tech.filatov.pigeoner.model.dovecote.Section;
import tech.filatov.pigeoner.model.dovecote.SectionType;
import tech.filatov.pigeoner.model.pigeon.Pigeon;
import tech.filatov.pigeoner.model.pigeon.Sex;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Component
public class PigeonValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Pigeon.class.equals(clazz);
    }

    public void validate(
            Object target,
            Errors errors,
            Map<Long, PigeonDto> descendants,
            Long fatherId,
            Long motherId
    ) {
        checkForLooping(errors, descendants, fatherId, motherId);
        validate(target, errors);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Pigeon pigeon = (Pigeon) target;

        checkParentsConstraints(pigeon, errors);
        checkSection(pigeon, errors);
        checkMate(pigeon, errors);
    }

    private void checkForLooping(
            Errors errors,
            Map<Long, PigeonDto> descendants,
            Long fatherId,
            Long motherId
    ) {
        if (fatherId != null && descendants.containsKey(fatherId)) {
            errors.rejectValue("father", "", "Голубь не может быть предком или потомком самому себе");
        }
        if (motherId != null && descendants.containsKey(motherId)) {
            errors.rejectValue("mother", "", "Голубь не может быть предком или потомком самому себе");
        }
    }

    private void checkParentsConstraints(Pigeon pigeon, Errors errors) {
        Pigeon father = pigeon.getFather();
        if (father != null) {
            checkNotOlderThan(father, pigeon, errors);
            checkParentSex(father, Sex.MALE, errors);
        }
        Pigeon mother = pigeon.getMother();
        if (mother != null) {
            checkNotOlderThan(mother, pigeon, errors);
            checkParentSex(mother, Sex.FEMALE, errors);
        }
    }

    private void checkSection(Pigeon pigeon, Errors errors) {
        Section section = pigeon.getSection();
        if (!pigeon.isOwn() || section == null
                || section.getType() != SectionType.NEST) {
            return;
        }

        Set<Pigeon> pigeonsInNest = section.getPigeons();
        switch (pigeonsInNest.size()) {
            case 1 -> {
                if (pigeon.getSex() == null) {
                    return;
                }
                if (pigeonsInNest.iterator().next().getSex() == pigeon.getSex()) {
                    errors.rejectValue("section", "", "В гнезде не могут находится голуби одного пола");
                }
            }
            case 2 -> {
                for (Pigeon pigeonInNest : pigeonsInNest) {
                    if (pigeonInNest.getId().equals(pigeon.getId())) {
                        return;
                    }
                }
                errors.rejectValue("section", "", "В гнезде уже есть 2 голубя");
            }
        }
    }

    private void checkMate(Pigeon pigeon, Errors errors) {
        if (pigeon.getSex() == null) return;
        Pigeon mate = pigeon.getMate();
        if (mate != null && mate.getSex() != null
                && mate.getSex() == pigeon.getSex()) {
            errors.rejectValue("mate", "", "Пара может быть только разнополой");
        }
    }

    private void checkParentSex(Pigeon parent, Sex sex, Errors errors) {
        if (parent != null && parent.getSex() != null
                && parent.getSex() != sex) {
            String field = sex == Sex.MALE ? "father" : "mother";
            errors.rejectValue(field, "", "Родитель должен быть соответствующего пола");
        }
    }

    private void checkNotOlderThan(Pigeon parent, Pigeon pigeon1, Errors errors) {
        LocalDate fathersBirthdate = parent != null ? parent.getBirthdate() : null;
        if (pigeon1.getBirthdate() != null && fathersBirthdate != null
                && fathersBirthdate.isAfter(pigeon1.getBirthdate())) {
            errors.rejectValue("birthdate", "", "Голубь не должен быть старше родителя");
        }
    }
}
