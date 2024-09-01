package grp1.config;

import grp1.user.User;
import grp1.user.UserRequest;
import grp1.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, UserDetailsService userDetailsService) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/note/**").authenticated()
                                .requestMatchers(HttpMethod.GET,"/").permitAll()
                                .requestMatchers("/register").permitAll()
                                .anyRequest().permitAll()
                                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(loginConfigurer -> loginConfigurer.loginPage("/login").permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(userDetailsService)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return username -> {
            User user = userService.findByName(username);
            return org.springframework.security.core.userdetails.User.withUsername(username)
                    .password(user.getPassword())
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
