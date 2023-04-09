package tech.filatov.pigeoner.model.flight;

public enum AfterFlightCondition {
    NORMAL("Нормальное"),
    TIRED("Уставший");

    public final String label;

    AfterFlightCondition(String label) {
        this.label = label;
    }

    public static AfterFlightCondition valueOfLabel(String label) {
        for (AfterFlightCondition c : values()) {
            if (c.label.equals(label)) {
                return c;
            }
        }
        return null;
    }
}
