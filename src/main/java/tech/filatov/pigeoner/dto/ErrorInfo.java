package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorInfo {
    private String field;
    private String value;
    private String message;
    private String shortMessage;

    public ErrorInfo(String message) {
        this.message = message;
    }

    public ErrorInfo(String field, String value, String message) {
        this.field = field;
        this.value = value;
        this.message = message;
    }
}
