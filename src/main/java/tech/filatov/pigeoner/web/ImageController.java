package tech.filatov.pigeoner.web;

import com.google.common.io.Files;
import com.google.common.net.HttpHeaders;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import tech.filatov.pigeoner.AuthorizedUser;
import tech.filatov.pigeoner.service.ImageService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pigeons/{pigeonId}/image")
public class ImageController {

    private final AuthorizedUser authUser = new AuthorizedUser();
    private final ImageService service;

    public ImageController(ImageService service) {
        this.service = service;
    }

    @GetMapping
    public List<String> getUploadedImages(@PathVariable long pigeonId) {
        return service.loadAll(authUser.getId(), pigeonId)
                .map(path -> MvcUriComponentsBuilder.fromMethodName(
                        ImageController.class,
                        "serveImage",
                        pigeonId, path.getFileName().toString()).build().toUri().toString())
                .toList();
    }

    @GetMapping("/main")
    public String getMain(@PathVariable long pigeonId) {
        return null;
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable long pigeonId, @PathVariable String filename) {
        Resource image = service.loadAsResource(filename, authUser.getId(), pigeonId);
        String imageType = Files.getFileExtension(filename);
        MediaType contentType = imageType.equals("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + image.getFilename() + "\""
                )
                .contentType(contentType).body(image);
    }

    @DeleteMapping("/{filename:.+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long pigeonId, @PathVariable String filename) {
        service.delete(filename, pigeonId, authUser.getId());
    }

    public ResponseEntity<Resource> getAllForPigeon(@PathVariable long pigeonId) {
        List<Resource> images = service.loadAllAsResources(authUser.getId(), pigeonId);
        Resource toSend = images.get(0);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment").body(toSend);
    }
}
