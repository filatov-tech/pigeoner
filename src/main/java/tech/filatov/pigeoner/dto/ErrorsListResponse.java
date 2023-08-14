package tech.filatov.pigeoner.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ErrorsListResponse {
    private List<ErrorInfo> errors;
}
