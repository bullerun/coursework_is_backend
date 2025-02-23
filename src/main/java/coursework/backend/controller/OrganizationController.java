package coursework.backend.controller;


import coursework.backend.dto.ErrorResponse;
import coursework.backend.dto.organization.InviteRequest;
import coursework.backend.dto.organization.OrganizationInvitesResponse;
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
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
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
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    public ResponseEntity<Organization> createOrganization(@RequestBody @Valid OrganizationRequestDTO request) {
        return ResponseEntity.ok(organizationService.createOrganization(request));
    }


    @PostMapping(value = "/invite/add", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Add invite to organization",
            description = "Create a new invite to an organization with a specified ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Invite created successfully",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Organization with stated ID not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    public ResponseEntity<String> addEmployee(@RequestBody @Valid InviteRequest request) {
        organizationService.createOrganizationInvite(request);
        return ResponseEntity.ok("success");
    }

    @PostMapping(value = "/invite/{invitationId}/accept", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Accept invite",
            description = "Accept organization invite with a specified ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Invite accepted successfully",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Invite with stated ID not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    public ResponseEntity<String> acceptInvitation(@PathVariable UUID invitationId) {
        organizationService.acceptInvite(invitationId);
        return ResponseEntity.ok("Приглашение принято");
    }

    @PostMapping(value = "/invite/{invitationId}/reject", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Reject invite",
            description = "Reject organization invite with a specified ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Invite rejected successfully",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Invite with stated ID not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    public ResponseEntity<String> rejectInvitation(@PathVariable UUID invitationId) {
        organizationService.declineInvite(invitationId);
        return ResponseEntity.ok("Приглашение отклонено");
    }

    @GetMapping(value = "/invite/myInvitations", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get current user's invitations",
            description = "Get a list of all organization invitations sent to current user",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Invites retrieved successfully",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<List<OrganizationInvitesResponse>> myInvitations() {
        return ResponseEntity.ok(organizationService.getMyInvitations());
    }

    @GetMapping(value = "/invite/myInvite", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get current user's sent invites",
            description = "Get a list of all organization invitations sent by current user",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Invites retrieved successfully",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<List<OrganizationInvitesResponse>> myInvites() {
        return ResponseEntity.ok(organizationService.getMyInvites());
    }

    @PostMapping("/updateRole")
    public ResponseEntity<String> updateRole(@RequestBody @Valid InviteRequest request) {
        return ResponseEntity.ok(organizationService.updateRole(request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Organization>> getMy(){
        return ResponseEntity.ok(organizationService.getMyOrganizations());
    }
}

