package tech.filatov.pigeoner.model.flight;

public enum AfterFlightCondition {
    NORMAL("Нормальное"),
    TIRED("Уставший");

    private final String title;

    AfterFlightCondition(String title) {
        this.title = title;
    }

    public static AfterFlightCondition valueOfLabel(String label) {
        for (AfterFlightCondition c : values()) {
            if (c.title.equals(label)) {
                return c;
            }
        }
        return null;
    }

    public String getTitle() {
        return title;
    }
}
