package hr.foi.diplomski.central.service.google;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;
import hr.foi.diplomski.central.controllers.api.processing.data.ImageProcessingResult;
import hr.foi.diplomski.central.exceptions.BadRequestException;
import hr.foi.diplomski.central.service.google.requestmodels.ImageContentRequestModel;
import hr.foi.diplomski.central.service.google.requestmodels.ImageTextContentRequestModel;
import hr.foi.diplomski.central.service.google.responsesmodels.GoogleAuthResponse;
import hr.foi.diplomski.central.service.google.responsesmodels.ImageContentModel;
import hr.foi.diplomski.central.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static hr.foi.diplomski.central.utils.CommonUtils.IMAGE_TEXT_RECOGNITION_REGEX;

@Service
@RequiredArgsConstructor
public class GoogleService {

    private final RestTemplate restTemplate;
    private final Gson gson;

    @Value("${google.image.content.api}")
    private String googleImageContentApi;

    @Value("${google.image.text.api}")
    private String googleImageTextApi;

    public ImageProcessingResult processImage(MultipartFile multipartFile) {
        try {
            String token = makeGoogleAuthentication();

            String imageBase64 = Base64.getEncoder().encodeToString(multipartFile.getBytes());

            String content = recognizeImageContent(token, imageBase64);
            String text = recognizeImageText(imageBase64);
            List<String> data = extractDataFromResponse(text);

            if (StringUtils.isNotBlank(content)) {
                return createResponse(content, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new BadRequestException("Slika nije uspješno prepoznata");
    }

    private String recognizeImageContent(String token, String imageBase64) throws IOException {
        String recognized = "";

        ImageContentRequestModel requestData = new ImageContentRequestModel(imageBase64);

        HttpEntity<String> httpEntity = new HttpEntity<>(gson.toJson(requestData), createAuthorizedHeaders(token));

        ImageContentModel response = restTemplate.postForObject(googleImageContentApi, httpEntity, ImageContentModel.class);

        if (response != null) {
            recognized = response.getImageContentName();
        }

        return recognized;
    }

    private String recognizeImageText(String imageBase64) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ImageTextContentRequestModel request = new ImageTextContentRequestModel(imageBase64);

        HttpEntity<String> httpEntity = new HttpEntity<>(gson.toJson(request), headers);

        String response = restTemplate.postForObject(googleImageTextApi, httpEntity, String.class);

        return removeUrlSave(response);
    }

    private HttpHeaders createAuthorizedHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    private String makeGoogleAuthentication() {
        try {
            File file = new ClassPathResource("google-client.json").getFile();

            GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(file));
            RSAPrivateKey privateKey = (RSAPrivateKey) credential.getServiceAccountPrivateKey();
            Algorithm algorithm = Algorithm.RSA256(null, privateKey);

            GoogleClientModel googleClientModel = new Gson().fromJson(FileUtils.readFileToString(file, StandardCharsets.UTF_8),
                    GoogleClientModel.class);

            long now = System.currentTimeMillis();

            String signedJwt = JWT.create()
                    .withClaim("iss", googleClientModel.getClientEmail())
                    .withClaim("scope", "https://www.googleapis.com/auth/cloud-platform")
                    .withClaim("aud", "https://www.googleapis.com/oauth2/v4/token")
                    .withClaim("iat", new Date(now))
                    .withClaim("exp", new Date(now + 3600 * 1000L))
                    .sign(algorithm);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String request = "{\"grant_type\":\"urn:ietf:params:oauth:grant-type:jwt-bearer\",\"assertion\":\"" + signedJwt + "\"}";

            HttpEntity<String> entity = new HttpEntity<>(request, headers);

            String response = restTemplate.postForObject("https://www.googleapis.com/oauth2/v4/token", entity, String.class);

            GoogleAuthResponse googleAuthResponse = new Gson().fromJson(response, GoogleAuthResponse.class);


            if (StringUtils.isNotBlank(googleAuthResponse.getAccessToken())) {
                return googleAuthResponse.getAccessToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private ImageProcessingResult createResponse(String type, List<String> data) {
        ImageProcessingResult result = new ImageProcessingResult();
        result.setType(type);

        if (data.size() == 3) {
            result.setPar1(data.get(0));
            result.setPar2(format(data.get(1)));
            result.setPar3(format(data.get(2)));
        }

        return result;
    }

    private Long format(String s) {
        if (StringUtils.isNumeric(s)) {
            return Long.valueOf(s);
        }

        return null;
    }

    private String removeUrlSave(String r) {
        if (StringUtils.isNotEmpty(r)) {
            r = r.replace("\\u003c", "<");
            r = r.replace("\\u003e", ">");

            return r;
        }

        return "";
    }

    private List<String> extractDataFromResponse(String response) {
        List<String> matches = Pattern.compile(IMAGE_TEXT_RECOGNITION_REGEX)
                .matcher(response)
                .results()
                .map(MatchResult::group).collect(Collectors.toList());

        if (matches.isEmpty()) {
            return Collections.emptyList();
        } else {
            return CommonUtils.dataFromSyntax(IMAGE_TEXT_RECOGNITION_REGEX, matches.get(0));
        }
    }

}
