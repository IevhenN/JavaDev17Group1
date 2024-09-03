package grp1.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        if(userRequest.getUsername().length() < USER_NAME_MIN_LENGTH || userRequest.getUsername().length() >= USER_NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(format("Username must be between {0} and {1} characters", USER_NAME_MIN_LENGTH, USER_NAME_MAX_LENGTH));
        }
        if(userRequest.getPassword().length() < USER_PASS_MIN_LENGTH || userRequest.getPassword().length() >= USER_PASS_MAX_LENGTH) {
            throw new IllegalArgumentException(format("Password must be between {0} and {1} characters", USER_PASS_MIN_LENGTH, USER_PASS_MAX_LENGTH));
        }
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new IllegalArgumentException(format("User with that name {0} already exists", userRequest.getUsername()));
        }
    }

    public User findByName(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        return byUsername.orElseThrow();
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public User getCurrentUser() {
        return userRepository.findByUsername(getCurrentUsername()).orElseThrow();
    }

}
