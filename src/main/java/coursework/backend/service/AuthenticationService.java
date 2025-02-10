package coursework.backend.service;



import coursework.backend.authentication.service.JwtService;
import coursework.backend.dto.JwtAuthenticationResponse;
import coursework.backend.dto.SignInRequest;
import coursework.backend.dto.SignUpRequest;
import coursework.backend.entity.User;
import coursework.backend.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = User.builder().username(request.getUsername()).password(passwordEncoder.encode(request.getPassword())).email(request.getEmail()).build();
        user = userService.create(user);
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(user.getId(), user.getUsername(), jwt, user.getRole());
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            throw new ForbiddenException("Invalid username or password.");
        }
        var userDetails = userService.getByUsername(request.getUsername());
        var jwt = jwtService.generateToken(userDetails);
        return new JwtAuthenticationResponse(userDetails.getId(), userDetails.getUsername(), jwt, userDetails.getRole());
    }


}