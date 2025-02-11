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

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        return save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public String getCurrentUserUsername() {
        var UserDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(UserDetails.toString());
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    public User getCurrentUser() {
        return getByUsername(getCurrentUserUsername());
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("user not found")
        );
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public void setRole(UUID id, Role role) {
        repository.updateRole(id, role);
    }

    public void removeAdmin(UUID id) {
        repository.updateRole(id, Role.ROLE_USER);
    }

    public User findById(UUID userId) {
        return repository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

    }

    public boolean isUserInOrganization(String username, UUID organization) {
        return repository.existsByUserAndOrganization(username, organization);
    }
}