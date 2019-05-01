package hr.foi.diplomski.central.service.google.requestmodels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class ImageTextContentRequestModel {

    private RequestClass requests;

    public ImageTextContentRequestModel(String imageBase64) {
        requests = new RequestClass(imageBase64);
    }

    @Data
    public class RequestClass {
        private List<FeaturesClass> features;
        private ImageClass image;

        public RequestClass(String imageBase64) {
            features = Collections.singletonList(new FeaturesClass());
            image = new ImageClass(imageBase64);
        }
    }

    @Data
    public class FeaturesClass {
        private Integer maxResults;
        private String type;

        public FeaturesClass() {
            this.maxResults = 50;
            this.type = "TEXT_DETECTION";
        }
    }

    @Data
    @AllArgsConstructor
    public class ImageClass {
        private String content;
    }
}
