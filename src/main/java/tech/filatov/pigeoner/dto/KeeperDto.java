package tech.filatov.pigeoner.dto;

import lombok.Getter;

@Getter
public class KeeperDto {

    private Long id;
    private String name;

    public KeeperDto() {
    }

    public KeeperDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
