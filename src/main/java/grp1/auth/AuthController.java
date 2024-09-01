package grp1.auth;

import grp1.user.UserRequest;
import grp1.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

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
    public String registerSubmit(@ModelAttribute UserRequest user, Model model) {
        try {
            userService.createUser(user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
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
