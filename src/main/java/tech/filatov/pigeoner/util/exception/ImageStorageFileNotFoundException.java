package tech.filatov.pigeoner.util.exception;

public class ImageStorageFileNotFoundException extends ImageStorageException {

    public ImageStorageFileNotFoundException(String message) {
        super(message);
    }

    public ImageStorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
