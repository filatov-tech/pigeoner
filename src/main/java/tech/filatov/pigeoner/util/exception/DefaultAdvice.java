package tech.filatov.pigeoner.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tech.filatov.pigeoner.dto.ErrorInfo;
import tech.filatov.pigeoner.dto.ErrorsListResponse;

import java.util.List;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(FilterContradictionException.class)
    public ResponseEntity<ErrorInfo> handleException(FilterContradictionException e) {
        ErrorInfo response = new ErrorInfo(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ValidationException e) {
        ErrorsListResponse response = new ErrorsListResponse();
        List<ErrorInfo> errors = e.getErrors().getFieldErrors()
                .stream()
                .map(error -> new ErrorInfo(error.getField(), error.getDefaultMessage()))
                .toList();

        response.setErrors(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
