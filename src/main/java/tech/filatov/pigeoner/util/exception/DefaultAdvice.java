package tech.filatov.pigeoner.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tech.filatov.pigeoner.dto.ErrorInfo;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(FilterContradictionException.class)
    public ResponseEntity<ErrorInfo> handleException(FilterContradictionException e) {
        ErrorInfo response = new ErrorInfo(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
