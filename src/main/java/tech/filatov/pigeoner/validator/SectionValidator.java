package tech.filatov.pigeoner.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import tech.filatov.pigeoner.model.dovecote.Section;
import tech.filatov.pigeoner.model.dovecote.SectionType;

import java.util.Objects;

@Component
public class SectionValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Section.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Section section = (Section) target;
        Section parent = section.getParent();
        SectionType parentType = parent == null ? null : parent.getType();


        switch (section.getType()) {
            case NEST -> {
                if (parent == null || parentType == SectionType.NEST) {
                    errors.rejectValue("parent", "", "Гнездо должно быть либо в голубятне, либо в одной из ее секций");
                }
            }
            case ROOM -> {
                if (parent == null || parentType == SectionType.NEST) {
                    errors.rejectValue("parent", "", "Секция может быть либо частью голубятни, либо частью другой секции");
                }
            }
            case DOVECOTE -> {
                if (parent != null) {
                    errors.rejectValue("parent", "", "Голубятня не может быть частью другой голубятни или секции");
                }
            }
        }
        if (!section.isNew() && parent != null && Objects.equals(section.getId(), parent.getId())) {
            errors.rejectValue("parent", "", "Секция не может быть родительской сама себе");
        }
    }
}
