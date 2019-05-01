package hr.foi.diplomski.central.controllers.api.processing.data;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class ImageWrapper {

    @NotNull
    private MultipartFile multipartFile;

}
