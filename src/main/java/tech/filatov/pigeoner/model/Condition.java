package tech.filatov.pigeoner.model;


public enum Condition {
    HEALTH("Здоров"),
    DISEASED("Болен"),
    DEAD("Умер"),
    LOST("Потерян");

    public final String label;

    Condition(String label) {
        this.label = label;
    }
}
