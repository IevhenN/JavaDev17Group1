package grp1.auth;

import grp1.user.UserRequest;
import grp1.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Locale;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    @Autowired
    private MessageSource messageSource;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserRequest());
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute UserRequest user, Model model, Locale locale) {
        try {
            userService.createUser(user);
        } catch (Exception e) {
            String localizedErrorMessage = messageSource.getMessage(e.getMessage(), null, locale);
            model.addAttribute("error", localizedErrorMessage);
            model.addAttribute("user", new UserRequest());
            return "register";
        }
        return "redirect:/login";
    }

    @RequestMapping("/")
    public String defaultPath(Principal principal) {
        if (principal != null) {
            return "redirect:/note/list";
        }
        return "redirect:/login";
    }

}
