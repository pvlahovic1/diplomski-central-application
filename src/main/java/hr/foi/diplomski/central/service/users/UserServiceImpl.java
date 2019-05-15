package hr.foi.diplomski.central.service.users;

import hr.foi.diplomski.central.controllers.api.users.data.UserDto;
import hr.foi.diplomski.central.exceptions.BadRequestException;
import hr.foi.diplomski.central.mappers.user.UserMapper;
import hr.foi.diplomski.central.model.User;
import hr.foi.diplomski.central.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + s + " not found!"));
    }

    @Override
    public User findCurrentUser() {
        var auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());

        return userRepository.findByUsername(auth.orElseThrow(() -> new BadRequestException("User is not logged in")).getName())
                .orElseThrow(() -> new BadRequestException("User is not found"));
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userMapper.entityToDto(userRepository.findAllByOrderByUsernameAsc());
    }

    @Override
    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException(String
                .format("Korisnik s id %s ne postoji", id)));

        return userMapper.entityToDto(user);
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = userMapper.dtoToEntity(userDto);

        return userMapper.entityToDto(userRepository.save(user));
    }

    @Override
    public UserDto activateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException(String
                .format("Korisnik s id %s ne postoji.", id)));

        if (user.isActive()) {
            throw new BadRequestException(String.format("Korisnik s id %s je već aktivan.", id));
        }

        user.setActive(true);
        return userMapper.entityToDto(userRepository.save(user));
    }

    @Override
    public UserDto deactivateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException(String
                .format("Korisnik s id %s ne postoji.", id)));

        if (!user.isActive()) {
            throw new BadRequestException(String.format("Korisnik s id %s je već neaktivan.", id));
        }

        user.setActive(false);
        return userMapper.entityToDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException(String
                .format("Korisnik s id %s ne postoji.", id)));

        if (user.equals(findCurrentUser())) {
            throw new BadRequestException("Korisnik ne može obirsati sam sebe.");
        }

        userRepository.delete(user);
    }

}
