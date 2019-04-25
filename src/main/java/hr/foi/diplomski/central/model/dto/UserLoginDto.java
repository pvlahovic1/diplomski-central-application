package hr.foi.diplomski.central.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserLoginDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDateTime expiration;
    private List<String> roles = new ArrayList<>();
    private String token;

}
