package tech.filatov.pigeoner.dto;

import lombok.Getter;

@Getter
public class KeeperDto extends BaseDto {

    private String name;

    public KeeperDto() {
    }

    public KeeperDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
