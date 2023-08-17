package tech.filatov.pigeoner.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ApiError {
    private String message;
    private HttpStatus status;
    private List<ErrorInfo> errors = new ArrayList<>();

    public ApiError(String message, HttpStatus status, List<ErrorInfo> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors == null ? Collections.emptyList() : errors;
    }

    public ApiError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
