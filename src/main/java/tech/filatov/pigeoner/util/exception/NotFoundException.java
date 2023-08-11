package tech.filatov.pigeoner.util.exception;

import java.util.function.Supplier;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public static Supplier<NotFoundException> withIdInfo(long id) {
        return () -> new NotFoundException(String.format("Entity with id=%d not found", id));
    }

    public static Supplier<NotFoundException> withNameInfo(String name) {
        return () -> new NotFoundException(String.format("Entity with name \"%s\" not found", name));
    }
}
