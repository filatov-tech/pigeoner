package tech.filatov.pigeoner.model.flight;

public enum FlightType {
    CUP("Кубковое соревнование"),
    COMPETITION("Соревнование"),
    TRAINING("Тренировка"),
    JUNIOR_COMPETITION("Юниорское соревнование");

    private final String title;

    FlightType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
