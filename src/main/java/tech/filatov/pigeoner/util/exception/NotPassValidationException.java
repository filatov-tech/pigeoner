package tech.filatov.pigeoner.util.exception;

import lombok.Getter;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;

import static tech.filatov.pigeoner.constant.Constants.VALIDATION_FAILED_MESSAGE;

@Getter
public class NotPassValidationException extends RuntimeException {
    private final Errors errors;
    private final List<FieldError> fieldErrors;

    public NotPassValidationException(Errors errors) {
        super(VALIDATION_FAILED_MESSAGE);
        this.errors = errors;
        this.fieldErrors = errors.getFieldErrors();
    }
}
