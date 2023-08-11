package tech.filatov.pigeoner.util.exception;

import org.springframework.validation.Errors;

public class ValidationException extends RuntimeException {
    private final Errors errors;

    public ValidationException(Errors errors) {
        super("Constraints violation - validation failed");
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
