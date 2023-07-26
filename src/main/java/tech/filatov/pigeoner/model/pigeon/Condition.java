package tech.filatov.pigeoner.model.pigeon;


import com.fasterxml.jackson.annotation.JsonValue;

public enum Condition {
    HEALTH("Здоров"),
    DISEASED("Болен"),
    DEAD("Умер"),
    LOST("Потерян");

    private final String title;

    Condition(String title) {
        this.title = title;
    }

    @JsonValue
    public String getTitle() {
        return title;
    }

    public static Condition valueOfLabel(String label) {
        for (Condition c : values()) {
            if (c.title.equals(label)) {
                return c;
            }
        }
        return null;
    }
}
