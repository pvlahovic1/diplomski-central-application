package hr.foi.diplomski.central.service.google.responsesmodels;

import lombok.Data;

import java.util.Comparator;
import java.util.List;

@Data
public class ImageContentModel {

    List<Content> payload;

    public String getImageContentName() {
        if (payload != null && !payload.isEmpty()) {
            payload.sort(Comparator.comparing(o -> o.getClassification().getScore()));

            return payload.get(0).getDisplayName();
        }

        return "";
    }

    @Data
    static class Content {
        private ContentClassification classification;
        private String displayName;
    }

    @Data
    static class ContentClassification {
        private Integer score;
    }

}
