package hr.foi.diplomski.central.service.users;

import hr.foi.diplomski.central.controllers.api.users.data.UserDto;
import hr.foi.diplomski.central.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findCurrentUser();

    List<UserDto> findAllUsers();

    UserDto findUserById(Long id);

    UserDto saveUser(UserDto userDto);

    UserDto activateUser(Long id);

    UserDto deactivateUser(Long id);

    void deleteUser(Long id);

}
