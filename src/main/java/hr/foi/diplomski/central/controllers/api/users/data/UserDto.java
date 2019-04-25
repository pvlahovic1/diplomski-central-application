package hr.foi.diplomski.central.controllers.api.users.data;

import hr.foi.diplomski.central.controllers.api.roles.data.RolesDto;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String firstName;
    private String password;
    private String lastName;
    private List<RolesDto> roles;
    private boolean active;

}
