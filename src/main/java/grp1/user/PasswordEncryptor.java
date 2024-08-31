package grp1.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncryptor {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "super_secret_password"; // Замените на пароль, который нужно зашифровать
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Зашифрованный пароль: " + encodedPassword);
    }
}
