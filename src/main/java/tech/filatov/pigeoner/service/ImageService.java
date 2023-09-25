package tech.filatov.pigeoner.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.filatov.pigeoner.config.ImageStorageProperties;
import tech.filatov.pigeoner.util.exception.ImageStorageException;
import tech.filatov.pigeoner.util.exception.ImageStorageFileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ImageService {

    private final Path rootDirectory;

    public ImageService(ImageStorageProperties properties) {
        this.rootDirectory = Paths.get(properties.getLocation());
    }

    public void store(MultipartFile image, long userId, long pigeonId) {
        try {
            if (image.isEmpty()) {
                throw new ImageStorageException("Ошибка сохранения пустого файла");
            }
            String originalFileName = image.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                throw new ImageStorageException("Имя файла не может быть null или пустым");
            }
            Path targetDirectory = this.rootDirectory
                    .resolve(String.valueOf(userId))
                    .resolve(String.valueOf(pigeonId))
                    .normalize().toAbsolutePath();
            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory);
            }
            Path destinationImage = targetDirectory
                    .resolve(image.getOriginalFilename())
                    .normalize().toAbsolutePath();
            if (!destinationImage.getParent().equals(
                    this.rootDirectory.resolve(String.valueOf(userId)).resolve(String.valueOf(pigeonId)).toAbsolutePath())) {
                throw new ImageStorageException("Нельзя сохранить изображение вне текущей папки");
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, destinationImage, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new ImageStorageException("Ошибка сохранения изображения", e);
        }
    }

    public void store(MultipartFile[] images, long userId, long pigeonId) {
        if (images.length == 0) {
            throw new ImageStorageException("Ошибка сохранения отсутствующих файлов");
        }
        for (MultipartFile image : images) {
            store(image, userId, pigeonId);
        }
    }

    public Stream<Path> loadAll(long userId, long pigeonId) {
        checkTargetDirectoryExist(userId, pigeonId);
        try {
            return Files.list(rootDirectory
                    .resolve(String.valueOf(userId))
                    .resolve(String.valueOf(pigeonId)));
        } catch (IOException e) {
            throw new ImageStorageException("Ошибка чтения сохраненных файлов");
        }
    }

    public Path load(String filename, long userId, long pigeonId) {
        checkTargetDirectoryExist(userId, pigeonId);
        return rootDirectory.resolve(String.valueOf(userId)).resolve(String.valueOf(pigeonId)).resolve(filename);
    }

    public List<Resource> loadAllAsResources(long userId, long pigeonId) {
        checkTargetDirectoryExist(userId, pigeonId);
        return loadAll(userId, pigeonId)
                .map(Path::toUri)
                .map(this::toUrlResource)
                .map(urlResource -> (Resource) urlResource)
                .toList();
    }

    public Resource loadAsResource(String filename, long userId, long pigeonId) {
        checkTargetDirectoryExist(userId, pigeonId);
        try {
            Path file = load(filename, userId, pigeonId);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new ImageStorageFileNotFoundException(
                        "Невозможно прочитать файл: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new ImageStorageFileNotFoundException("Невозможно прочитать файл: " + filename, e);
        }
    }

    public void delete(String filename, long userId, long pigeonId) {
        Path image = load(filename, userId, pigeonId);
        try {
            if (!Files.deleteIfExists(image)) {
                throw new ImageStorageFileNotFoundException(
                        String.format("Невозможно удалить. Файла \"%s\" не существует", image.getFileName())
                );
            }
        } catch (IOException e) {
            throw new ImageStorageException("Ошибка при попытке удалить изображение",e);
        }
    }

    private void checkTargetDirectoryExist(long userId, long pigeonId) {
        if (!Files.exists(
                rootDirectory
                        .resolve(String.valueOf(userId))
                        .resolve(String.valueOf(pigeonId))
                        .normalize().toAbsolutePath())
        ) {
            throw new ImageStorageException("Директория пользователя ещё не создана");
        }
    }

    private UrlResource toUrlResource(URI uri) {
        try {
            return new UrlResource(uri);
        } catch (MalformedURLException e) {
            throw new ImageStorageFileNotFoundException("Невозможно прочитать файл", e);
        }
    }
}
