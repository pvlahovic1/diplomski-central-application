package hr.foi.diplomski.central.service.users;

import hr.foi.diplomski.central.exceptions.BadRequestException;
import hr.foi.diplomski.central.model.User;
import hr.foi.diplomski.central.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + s + " not found!"));
    }

    public User findCurrentUser() {
        var auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());

        return userRepository.findByUsername(auth.orElseThrow(() -> new BadRequestException("User is not logged in")).getName())
                .orElseThrow(() -> new BadRequestException("User is not found"));
    }
}
