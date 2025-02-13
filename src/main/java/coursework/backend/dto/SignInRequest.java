package coursework.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {
    @NotBlank(message = "Username can not be blank")
    private String username;
    @NotBlank(message = "Password can not be blank")
    private String password;
}