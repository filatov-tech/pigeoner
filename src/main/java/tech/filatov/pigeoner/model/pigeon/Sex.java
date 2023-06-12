package tech.filatov.pigeoner.model.pigeon;

public enum Sex {
    MALE("male"),
    FEMALE("female");

    private final String title;

    Sex(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
