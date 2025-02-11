package coursework.backend.config;


import coursework.backend.entity.User;
import coursework.backend.entity.enums.Role;
import coursework.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Admin {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${ADMIN_USERNAME}")
    private String adminUsername;
    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            var user = User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminUsername)
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(user);
        }
    }

}
