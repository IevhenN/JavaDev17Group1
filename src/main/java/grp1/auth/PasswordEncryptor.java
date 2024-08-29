package grp1.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncryptor {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "user"; // Замените на пароль, который нужно зашифровать
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Зашифрованный пароль: " + encodedPassword);
    }
}
