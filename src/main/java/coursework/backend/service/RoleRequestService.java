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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RoleRequestService {

    private final RoleRequestRepository roleRequestRepository;
    private final UserService userService;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public void requestRoleChange(Role requestedRole) {
        User user = userService.getCurrentUser();
        System.out.println(user.getUsername());
        if (roleRequestRepository.existsByUserIdAndStatus(user.getId(), RequestStatus.PENDING)) {
            throw new RoleRequestConflictException("You already have an active request.");
        }

        RoleRequest request = new RoleRequest();
        request.setUserId(user.getId());
        request.setRequestedRole(requestedRole);
        request.setStatus(RequestStatus.PENDING);
        request = roleRequestRepository.save(request);
        kafkaProducerService.sendLog("RoleRequest created: " + request);
    }

    @Transactional
    public void approveRoleRequest(UUID requestId) {
        RoleRequest request = roleRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        request.setStatus(RequestStatus.APPROVED);
        request = roleRequestRepository.save(request);
        kafkaProducerService.sendLog("RoleRequest approved: " + request);

        userService.setRole(request.getUserId(), request.getRequestedRole());
        kafkaProducerService.sendLog("User " + request.getUserId() + " updated to " + request.getRequestedRole());
    }

    @Transactional
    public void rejectRoleRequest(UUID requestId) {
        RoleRequest request = roleRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));


        request.setStatus(RequestStatus.REJECTED);
        request = roleRequestRepository.save(request);
        kafkaProducerService.sendLog("RoleRequest rejected: " + request);
    }

    @Transactional
    public List<RoleRequest> getPendingRequests() {
        return roleRequestRepository.findByStatus(RequestStatus.PENDING);
    }


}
