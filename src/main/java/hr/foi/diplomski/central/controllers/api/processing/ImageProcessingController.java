package hr.foi.diplomski.central.controllers.api.processing;

import hr.foi.diplomski.central.controllers.api.processing.data.ImageProcessingResult;
import hr.foi.diplomski.central.controllers.api.processing.data.ImageWrapper;
import hr.foi.diplomski.central.service.google.GoogleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image-processing")
@Secured({"ROLE_ADMIN", "ROLE_USER"})
public class ImageProcessingController {

    private final GoogleService googleService;

    @PostMapping
    public ResponseEntity<ImageProcessingResult> processImage(@Valid @ModelAttribute ImageWrapper imageWrapper) {
        return ResponseEntity.ok(googleService.processImage(imageWrapper.getMultipartFile()));
    }

}
