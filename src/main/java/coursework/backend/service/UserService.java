package coursework.backend.service;


import coursework.backend.entity.enums.Role;
import coursework.backend.entity.User;
import coursework.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user)  {
        if (repository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        return save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public String getCurrentUserUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    public User getCurrentUser() {
        return getByUsername(getCurrentUserUsername());
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public void setAdmin(Long id) {
        repository.updateRole(id, Role.ROLE_ADMIN);
    }

    public void removeAdmin(Long id) {
        repository.updateRole(id, Role.ROLE_USER);
    }
}