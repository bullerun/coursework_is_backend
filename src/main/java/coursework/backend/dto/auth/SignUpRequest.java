package coursework.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank(message = "username не должен быть пустым")
    private String username;
    @NotBlank(message = "username не должен быть пустым")
    private String email;
    @NotBlank(message = "password не должен быть пустым")
    private String password;
}