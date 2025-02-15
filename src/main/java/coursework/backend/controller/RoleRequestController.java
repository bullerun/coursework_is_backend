package coursework.backend.controller;

import coursework.backend.dto.ErrorResponse;
import coursework.backend.entity.RoleRequest;
import coursework.backend.entity.enums.Role;
import coursework.backend.service.RoleRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class RoleRequestController {

    private final RoleRequestService roleRequestService;

    @PostMapping(value = "/change", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Send role change request",
            description = "Send a request to change the current role to the specified one",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role change request sent successfully",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Pending request already exists",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<String> requestRoleChange(@RequestParam Role role) {
        roleRequestService.requestRoleChange(role);
        return ResponseEntity.ok("Role change request sent.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{requestId}/approve", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Approve role request",
            description = "Approve a role change request with a specified ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Request approved successfully",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Request with stated ID not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<String> approveRequest(@PathVariable UUID requestId) {
        roleRequestService.approveRoleRequest(requestId);
        return ResponseEntity.ok("Request approved, role changed.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{requestId}/reject", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Reject role request",
            description = "Reject a role change request with a specified ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Request rejected successfully",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Request with stated ID not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<String> rejectRequest(@PathVariable UUID requestId) {
        roleRequestService.rejectRoleRequest(requestId);
        return ResponseEntity.ok("Request rejected.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/pending", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get pending requests",
            description = "Get all pending role change requests",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Requests retrieved successfully",
                            content = @Content(schema = @Schema(implementation = RoleRequest.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<List<RoleRequest>> getPendingRequests() {
        return ResponseEntity.ok(roleRequestService.getPendingRequests());
    }
}
