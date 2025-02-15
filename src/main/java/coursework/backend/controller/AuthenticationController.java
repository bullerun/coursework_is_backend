package coursework.backend.controller;

import coursework.backend.dto.auth.JwtAuthenticationResponse;
import coursework.backend.dto.auth.SignInRequest;
import coursework.backend.dto.auth.SignUpRequest;
import coursework.backend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Sign up",
            description = "User signing up",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User signed up successfully", content = @Content(schema = @Schema(implementation = JwtAuthenticationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Username already taken", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    public JwtAuthenticationResponse signUp(@Valid @RequestBody SignUpRequest sign) {
        return authenticationService.signUp(sign);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Sign in",
            description = "User signing in",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User signed in successfully", content = @Content(schema = @Schema(implementation = JwtAuthenticationResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    public JwtAuthenticationResponse signIn(@Valid @RequestBody SignInRequest sign) {
        return authenticationService.signIn(sign);
    }
}
