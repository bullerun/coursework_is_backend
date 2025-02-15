package coursework.backend.service;


import coursework.backend.entity.User;
import coursework.backend.entity.enums.Role;
import coursework.backend.exception.NotFoundException;
import coursework.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with this username already exists.");
        }
        user = save(user);
        kafkaProducerService.sendLog("User created: " + user);
        return user;
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found."));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public String getCurrentUserUsername() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    public User getCurrentUser() {
        return getByUsername(getCurrentUserUsername());
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found.")
        );
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public void setRole(UUID id, Role role) {
        repository.updateRole(id, role);
        kafkaProducerService.sendLog("User " + id + " role updated to " + role);
    }

    public void removeAdmin(UUID id) {
        repository.updateRole(id, Role.ROLE_USER);
        kafkaProducerService.sendLog("User " + id + " admin role removed");
    }

    public User findById(UUID userId) {
        return repository.findById(userId).orElseThrow(() -> new NotFoundException("User not found."));

    }

    public boolean isUserInOrganization(String username, UUID organization) {
        return repository.existsByUserAndOrganization(username, organization);
    }
}