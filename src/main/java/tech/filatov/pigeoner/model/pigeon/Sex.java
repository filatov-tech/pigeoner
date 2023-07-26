package tech.filatov.pigeoner.model.pigeon;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Sex {
    MALE("male"),
    FEMALE("female");

    private final String title;

    Sex(String title) {
        this.title = title;
    }

    @JsonValue
    public String getTitle() {
        return title;
    }
}
