package tech.filatov.pigeoner.model.pigeon;


public enum Condition {
    HEALTH("Здоров"),
    DISEASED("Болен"),
    DEAD("Умер"),
    LOST("Потерян");

    public final String label;

    Condition(String label) {
        this.label = label;
    }

    public static Condition valueOfLabel(String label) {
        for (Condition c : values()) {
            if (c.label.equals(label)) {
                return c;
            }
        }
        return null;
    }
}
