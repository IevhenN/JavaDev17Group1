package grp1.user;

import grp1.exception.NoteAppException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.text.MessageFormat.format;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public static final int USER_NAME_MIN_LENGTH = 5;
    public static final int USER_NAME_MAX_LENGTH = 50;
    public static final int USER_PASS_MIN_LENGTH = 8;
    public static final int USER_PASS_MAX_LENGTH = 100;
    private final UserRepository userRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String createUser(UserRequest userRequest) {
        validateUser(userRequest);
        User user = new User();
        user.setUsername(userRequest.getUsername());
        String pass = passwordEncoder.encode(userRequest.getPassword());
        user.setPassword(pass);
        LOGGER.info("User created: {}", user);

        userRepository.save(user);
        return "User created";
    }

    private void validateUser(UserRequest userRequest) {
        validateUserParams(userRequest);
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new NoteAppException("error.username.exists", userRequest.getUsername());
        }
    }

    private void validateUserParams(UserRequest userRequest) {
        if(userRequest.getUsername().length() < USER_NAME_MIN_LENGTH || userRequest.getUsername().length() >= USER_NAME_MAX_LENGTH) {
            throw new NoteAppException("error.username.empty", USER_NAME_MIN_LENGTH, USER_NAME_MAX_LENGTH);
        }
        if(userRequest.getPassword().length() < USER_PASS_MIN_LENGTH || userRequest.getPassword().length() >= USER_PASS_MAX_LENGTH) {
            throw new NoteAppException("error.password.empty", USER_PASS_MIN_LENGTH, USER_PASS_MAX_LENGTH);
        }
    }

    public void validateExistedUser(UserRequest userRequest) {
        validateUserParams(userRequest);
        User user = userRepository.findByUsername(userRequest.getUsername()).orElseThrow(() -> new NoteAppException("error.user.not_found", userRequest.getUsername()));
        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            throw new NoteAppException("error.password.invalid");
        }
    }

    public User findByName(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        return byUsername.orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public User getCurrentUser() {
        return userRepository.findByUsername(getCurrentUsername()).orElseThrow();
    }

}
