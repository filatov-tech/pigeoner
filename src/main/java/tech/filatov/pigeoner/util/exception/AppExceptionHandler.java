package tech.filatov.pigeoner.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tech.filatov.pigeoner.dto.ErrorInfo;
import tech.filatov.pigeoner.dto.ApiError;
import tech.filatov.pigeoner.util.CommonUtil;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tech.filatov.pigeoner.constant.Constants.VALIDATION_FAILED_MESSAGE;

@ControllerAdvice
public class AppExceptionHandler {

    private static final String SQLSTATE_UNIQUE_VIOLATION_STANDARD = "23505";

    @ExceptionHandler(FilterContradictionException.class)
    public ResponseEntity<ErrorInfo> handleException(FilterContradictionException e) {
        ErrorInfo response = new ErrorInfo(Collections.singletonList(e.getMessage()));
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
                e.getMessage(), HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException e) {
        ApiError response = new ApiError(
                e.getMessage(), HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiError> handleUniqueConstraintViolation(SQLException e) throws SQLException {
        if (!e.getSQLState().equals(SQLSTATE_UNIQUE_VIOLATION_STANDARD)) {
            throw e;
        }

        Map<String, String> fieldAndValue = extractEachFieldAndItsValueFrom(e);
        Map<String, ErrorInfo> errors = new HashMap<>();

        for (Map.Entry<String, String> fieldValueEntry : fieldAndValue.entrySet()) {
            String fieldName = fieldValueEntry.getKey();
            String value = fieldValueEntry.getValue();
            errors.put(
                    fieldName,
                    new ErrorInfo(
                            fieldName,
                            value,
                            List.of(String.format("Поле %s со значением %s нарушает уникальность сохраняемого объекта", fieldName, value)),
                            "Не уникально"
                    )
            );
        }

        ApiError response = new ApiError(
                "Не уникальный объект - такой объект уже есть", HttpStatus.BAD_REQUEST, errors
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    private Map<String, String> extractEachFieldAndItsValueFrom(SQLException e) {
        Map<String, String> fieldsErrorsMap = new HashMap<>();
        Pattern pattern = Pattern.compile(".*\\(([^)]+)\\)=\\(([^)]+)\\).*", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(e.getMessage());

        if (!matcher.matches()) {
            return fieldsErrorsMap;
        }

        String[] fields = Arrays
                .stream(matcher.group(1).split(", "))
                .map(CommonUtil::toCamelCase)
                .toArray(String[]::new);
        String[] values = matcher.group(2).split(", ");
        for (int i = 0; i < fields.length; i++) {
            fieldsErrorsMap.put(fields[i], values[i]);
        }

        return fieldsErrorsMap;
    }

    private Map<String, ErrorInfo> extractErrorsFrom(List<FieldError> errors) {
        Map<String, ErrorInfo> resultMap = new HashMap<>();
        for (FieldError error : errors) {
            String fieldName = error.getField();
            if (resultMap.containsKey(fieldName)) {
                resultMap.computeIfPresent(
                        fieldName,
                        (key, errorInfo) -> errorInfo.addMessageToList(error.getDefaultMessage()));
            } else {
                resultMap.put(fieldName, new ErrorInfo(
                        fieldName, "", Arrays.asList(error.getDefaultMessage())
                ));
            }
        }
        return resultMap;
    }
}

