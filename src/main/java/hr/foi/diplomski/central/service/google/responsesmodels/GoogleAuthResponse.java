package hr.foi.diplomski.central.service.google.responsesmodels;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GoogleAuthResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    private Long expiresIn;

    @SerializedName("token_type")
    private String tokenType;

}
