package coursework.backend.controller;

import coursework.backend.entity.RoleRequest;
import coursework.backend.entity.enums.Role;
import coursework.backend.service.RoleRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleRequestController {

    private final RoleRequestService roleRequestService;

    @PostMapping("/change")
    public ResponseEntity<String> requestRoleChange(@RequestParam Role role) {
        roleRequestService.requestRoleChange(role);
        return ResponseEntity.ok("Role change request sent.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{requestId}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable UUID requestId) {
        roleRequestService.approveRoleRequest(requestId);
        return ResponseEntity.ok("Request approved, role changed.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{requestId}/reject")
    public ResponseEntity<String> rejectRequest(@PathVariable UUID requestId) {
        roleRequestService.rejectRoleRequest(requestId);
        return ResponseEntity.ok("Request rejected.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<List<RoleRequest>> getPendingRequests() {
        return ResponseEntity.ok(roleRequestService.getPendingRequests());
    }
}
