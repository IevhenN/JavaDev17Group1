package grp1.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            return "User with that name already exists";
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        String pass = passwordEncoder.encode(userRequest.getPassword());
        user.setPassword(pass);
        System.out.println("pass = " + pass);

        userRepository.save(user);

        return "User created";
    }

    public User findByName(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);

        return byUsername.orElseThrow();
    }
}
