package tech.filatov.pigeoner.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tech.filatov.pigeoner.dto.ErrorInfo;
import tech.filatov.pigeoner.dto.ApiError;

import java.util.List;

import static tech.filatov.pigeoner.constant.Constants.VALIDATION_FAILED_MESSAGE;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(FilterContradictionException.class)
    public ResponseEntity<ErrorInfo> handleException(FilterContradictionException e) {
        ErrorInfo response = new ErrorInfo(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotPassValidationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(NotPassValidationException e) {
        ApiError response = new ApiError(
                e.getMessage(), HttpStatus.BAD_REQUEST, extractErrorsFrom(e.getErrors().getFieldErrors())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(MethodArgumentNotValidException e) {
        ApiError response = new ApiError(
                VALIDATION_FAILED_MESSAGE, HttpStatus.BAD_REQUEST, extractErrorsFrom(e.getFieldErrors())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private List<ErrorInfo> extractErrorsFrom(List<FieldError> errors) {
        return errors.stream()
                .map(error -> new ErrorInfo(error.getField(), error.getDefaultMessage()))
                .toList();
    }

}
