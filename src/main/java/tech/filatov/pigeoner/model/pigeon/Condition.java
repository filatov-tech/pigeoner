package tech.filatov.pigeoner.model.pigeon;


public enum Condition {
    HEALTH("Здоров"),
    DISEASED("Болен"),
    DEAD("Умер"),
    LOST("Потерян");

    private final String title;

    Condition(String title) {
        this.title = title;
    }

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
