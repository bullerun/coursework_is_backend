package coursework.backend.controller;


import coursework.backend.dto.ErrorResponse;
import coursework.backend.dto.organization.OrganizationInvitesResponse;
import coursework.backend.dto.organization.InviteRequest;
import coursework.backend.dto.organization.OrganizationRequestDTO;
import coursework.backend.entity.Organization;
import coursework.backend.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping("/new")
    @Operation(
            summary = "Create a new organization",
            description = "Creates a new organization and assigns a unique identifier",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Organization successfully created",
                            content = @Content(schema = @Schema(implementation = Organization.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<Organization> createOrganization(@RequestBody @Valid OrganizationRequestDTO request) {
        return ResponseEntity.ok(organizationService.createOrganization(request));
    }


    @PostMapping("/invite/add")
    public ResponseEntity<String> addEmployee(@RequestBody @Valid InviteRequest request) {
        organizationService.createOrganizationInvite(request);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/invite/{invitationId}/accept")
    public ResponseEntity<String> acceptInvitation(@PathVariable UUID invitationId) {
        organizationService.acceptInvite(invitationId);
        return ResponseEntity.ok("Приглашение принято");
    }

    @PostMapping("/invite/{invitationId}/reject")
    public ResponseEntity<String> rejectInvitation(@PathVariable UUID invitationId) {
        organizationService.declineInvite(invitationId);
        return ResponseEntity.ok("Приглашение отклонено");
    }

    @GetMapping("/invite/myInvitations")
    public ResponseEntity<List<OrganizationInvitesResponse>> myInvitations() {
        return ResponseEntity.ok(organizationService.getMyInvitations());
    }

    @GetMapping("/invite/myInvite")
    public ResponseEntity<List<OrganizationInvitesResponse>> iInvite() {
        return ResponseEntity.ok(organizationService.getMyInvites());
    }

}

