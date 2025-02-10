package coursework.backend.controller;

import coursework.backend.dto.JwtAuthenticationResponse;
import coursework.backend.dto.SignInRequest;
import coursework.backend.dto.SignUpRequest;
import coursework.backend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
