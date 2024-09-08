package grp1.auth;

import grp1.exception.NoteAppException;
import grp1.user.UserRequest;
import grp1.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.util.Locale;

import static grp1.auth.AuthController.SESSION_ATTRIBUTE_ERROR;

@RequiredArgsConstructor
public class AuthFailureHandler implements AuthenticationFailureHandler {

    private final UserService userService;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(request.getParameter("username"));
        userRequest.setPassword(request.getParameter("password"));
        try {
            userService.validateExistedUser(userRequest);
            request.getSession().setAttribute(SESSION_ATTRIBUTE_ERROR, exception.getMessage());
        } catch (NoteAppException e) {
            Locale locale = localeResolver.resolveLocale(request);
            request.getSession().setAttribute(SESSION_ATTRIBUTE_ERROR, e.getLocalizedMessage(messageSource, locale));
        }
        response.sendRedirect("/login");
    }
}
