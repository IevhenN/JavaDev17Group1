package grp1.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

    @NotEmpty(message = "{error.username.empty}")
    @Size(min = 5, max = 50, message = "{error.username.empty}")
    private String username;

    @NotEmpty(message = "{error.password.empty}")
    @Size(min = 8, max = 100, message = "{error.password.empty}")
    private String password;
}
