package hr.foi.diplomski.central.service.google.requestmodels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
public class ImageContentRequestModel implements Serializable {

    private ImageClass payload;

    public ImageContentRequestModel(String imageBytes) {
        payload = new ImageClass(imageBytes);
    }

    @Data
    @AllArgsConstructor
    class ImageBytesClass {
        private String imageBytes;
    }

    @Data
    class ImageClass {
        private ImageBytesClass image;

        public ImageClass(String imageBytes) {
            this.image = new ImageBytesClass(imageBytes);
        }
    }

}
