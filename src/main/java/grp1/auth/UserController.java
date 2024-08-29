package grp1.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String newUser(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(UserRequest userRequest) {
        String result = userService.createUser(userRequest);
        if ("User created".equals(result)) {
            return "redirect:/note/list";
        } else {
            return "signup";
        }
    }
}
