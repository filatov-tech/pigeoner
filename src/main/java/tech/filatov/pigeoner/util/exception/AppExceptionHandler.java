package tech.filatov.pigeoner.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tech.filatov.pigeoner.dto.ErrorInfo;
import tech.filatov.pigeoner.dto.ApiError;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tech.filatov.pigeoner.constant.Constants.VALIDATION_FAILED_MESSAGE;

@ControllerAdvice
public class AppExceptionHandler {

    private static final String SQLSTATE_UNIQUE_VIOLATION_STANDART = "23505";

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
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(IllegalRequestDataException.class)
    public ResponseEntity<ApiError> handleIllegalRequestData(IllegalRequestDataException e) {
        ApiError response = new ApiError(
                e.getMessage(), HttpStatus.BAD_REQUEST, null
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException e) {
        ApiError response = new ApiError(
                e.getMessage(), HttpStatus.NOT_FOUND, null
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiError> handleUniqueConstraintViolation(SQLException e) throws SQLException {
        if (!e.getSQLState().equals(SQLSTATE_UNIQUE_VIOLATION_STANDART)) {
            throw e;
        }

        String[] fieldAndValue = extractFieldAndValueFrom(e);
        String field = fieldAndValue[0];
        String value = fieldAndValue[1];
        List<ErrorInfo> errors = new ArrayList<>();
        errors.add(
                new ErrorInfo(
                        field,
                        String.format("Поле %s со значением %s уже существует", field, value)
                ));

        ApiError response = new ApiError(
                "Нарушена уникальность одного из полей", HttpStatus.BAD_REQUEST, errors
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    private String[] extractFieldAndValueFrom(SQLException e) {
        String[] fieldAndValue = new String[2];
        Pattern pattern = Pattern.compile("\\(([a-zA-Zа-яА-Я_ \\d-]+)\\)");
        Matcher matcher = pattern.matcher(e.getMessage());

        for (int i = 0; matcher.find(); i++) {
            fieldAndValue[i] = matcher.group(1);
        }
        return fieldAndValue;
    }

    private List<ErrorInfo> extractErrorsFrom(List<FieldError> errors) {
        return errors.stream()
                .map(error -> new ErrorInfo(error.getField(), error.getDefaultMessage()))
                .toList();
    }

}
