package coursework.backend.controller;

import coursework.backend.dto.auth.JwtAuthenticationResponse;
import coursework.backend.dto.auth.SignInRequest;
import coursework.backend.dto.auth.SignUpRequest;
import coursework.backend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public JwtAuthenticationResponse signUp(@Valid @RequestBody SignUpRequest sign) {
        return authenticationService.signUp(sign);
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse signIn(@Valid @RequestBody SignInRequest sign) {
        return authenticationService.signIn(sign);
    }
}
