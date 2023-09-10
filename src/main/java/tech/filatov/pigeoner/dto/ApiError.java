package tech.filatov.pigeoner.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.*;

@Getter
@Setter
public class ApiError {
    private String message;
    private HttpStatus status;
    private Map<String, ErrorInfo> fields = new HashMap<>();

    public ApiError(String message, HttpStatus status, Map<String, ErrorInfo> fieldsWithErrors) {
        this.message = message;
        this.status = status;
        this.fields = fieldsWithErrors == null ? Collections.emptyMap() : fieldsWithErrors;
    }

    public ApiError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
