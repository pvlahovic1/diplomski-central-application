package hr.foi.diplomski.central.service.google;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GoogleClientModel {

    @SerializedName("type")
    private String type;

    @SerializedName("project_id")
    private String projectId;

    @SerializedName("private_key_id")
    private String privateKeyId;

    @SerializedName("private_key")
    private String privateKey;

    @SerializedName("client_email")
    private String clientEmail;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("auth_uri")
    private String authUri;

    @SerializedName("token_uri")
    private String tokenUri;

    @SerializedName("auth_provider_x509_cert_url")
    private String authProviderX509CertUrl;

    @SerializedName("client_x509_cert_url")
    private String clientX509CertUrl;

}
