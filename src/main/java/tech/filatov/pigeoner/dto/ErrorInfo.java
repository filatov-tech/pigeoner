package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorInfo {
    private String field;
    private String value;
    private List<String> messages = new ArrayList<>();
    private String shortMessage;

    public ErrorInfo(List<String> messages) {
        this.messages = messages == null ? new ArrayList<>() : messages;
    }

    public ErrorInfo(String field, String value, List<String> messages) {
        this.field = field;
        this.value = value;
        this.messages = messages == null ? new ArrayList<>() : messages;
    }

    public ErrorInfo addMessageToList(String message) {
        this.messages.add(message);
        return this;
    }
}
