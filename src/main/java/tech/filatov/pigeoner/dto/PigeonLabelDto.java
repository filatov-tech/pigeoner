package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PigeonLabelDto {
    private Long id;
    private String ringNumber;
    private boolean isMale;
    private Long sectionId;
}
