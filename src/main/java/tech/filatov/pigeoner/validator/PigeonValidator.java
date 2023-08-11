package tech.filatov.pigeoner.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

@Component
public class PigeonValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Pigeon.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
