package coursework.backend.service;

import coursework.backend.entity.RoleRequest;
import coursework.backend.entity.User;
import coursework.backend.entity.enums.RequestStatus;
import coursework.backend.entity.enums.Role;
import coursework.backend.exception.NotFoundException;
import coursework.backend.exception.RoleRequestConflictException;
import coursework.backend.repository.RoleRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RoleRequestService {

    private final RoleRequestRepository roleRequestRepository;
    private final UserService userService;

    public void requestRoleChange(Role requestedRole) {
        User user = userService.getCurrentUser();
        System.out.println(user.getUsername());
        if (roleRequestRepository.existsByUserIdAndStatus(user.getId(), RequestStatus.PENDING)) {
            throw new RoleRequestConflictException("У вас уже есть активный запрос");
        }

        RoleRequest request = new RoleRequest();
        request.setUserId(user.getId());
        request.setRequestedRole(requestedRole);
        request.setStatus(RequestStatus.PENDING);
        roleRequestRepository.save(request);
    }

    public void approveRoleRequest(UUID requestId) {
        RoleRequest request = roleRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));

        request.setStatus(RequestStatus.APPROVED);
        roleRequestRepository.save(request);

        userService.setRole(request.getUserId(), request.getRequestedRole());
    }

    public void rejectRoleRequest(UUID requestId) {
        RoleRequest request = roleRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));


        request.setStatus(RequestStatus.REJECTED);
        roleRequestRepository.save(request);
    }

    public List<RoleRequest> getPendingRequests() {
        return roleRequestRepository.findByStatus(RequestStatus.PENDING);
    }


}
