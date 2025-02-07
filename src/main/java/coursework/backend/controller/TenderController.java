package coursework.backend.controller;

import coursework.backend.dto.ErrorResponse;
import coursework.backend.dto.TenderRequestDTO;
import coursework.backend.dto.TenderResponseDTO;
import coursework.backend.entity.enums.TenderStatus;
import coursework.backend.exception.ForbiddenException;
import coursework.backend.exception.NotFoundException;
import coursework.backend.exception.UnauthorizedException;
import coursework.backend.service.TenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tenders")
@RequiredArgsConstructor
@Tag(name = "Tender Management", description = "API for managing tenders")
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class TenderController {

    private final TenderService tenderService;

    //TODO: !getTenders!,

    @GetMapping(value = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Ping the server",
            description = "Checks if the server is up and running",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponse(responseCode = "200", description = "Server is up", content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("ok");
    }


    @GetMapping
    @Operation(summary = "Get user tenders",
            description = "Retrieve tenders associated with a specific user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of tenders retrieved"),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<List<TenderResponseDTO>> getTenders(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "asc", required = false)
            @Pattern(regexp = "asc|desc", message = "sortDirection должен быть 'asc' или 'desc'") String sortDirection) {
        return ResponseEntity.ok(tenderService.getAllTenders(page, pageSize, sortDirection));
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a new tender",
            description = "Creates a new tender and assigns a unique identifier and creation time",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tender successfully created", content = @Content(schema = @Schema(implementation = TenderResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<TenderResponseDTO> createTender(@RequestBody @Valid TenderRequestDTO request) {
        return ResponseEntity.ok(tenderService.createTender(request));
    }

    @GetMapping(value = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get user tenders",
            description = "Retrieve tenders associated with a specific user",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of user tenders retrieved"),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<List<TenderResponseDTO>> getUserTenders() {
        return ResponseEntity.ok(tenderService.getUserTenders());
    }

    @GetMapping(value = "/{tenderId}/status", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Get tender status",
            description = "Retrieve the current status of a specific tender",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tender status retrieved", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Tender does not exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    public ResponseEntity<String> getTenderStatus(
            @Parameter(description = "ID of the tender to retrieve status for")
            @PathVariable UUID tenderId) {
        return ResponseEntity.ok(tenderService.getTenderStatus(tenderId));
    }

    @PutMapping(value = "/{tenderId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update tender status",
            description = "Update the status of a specific tender",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tender status successfully updated", content = @Content(schema = @Schema(implementation = TenderResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Tender does not exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    public ResponseEntity<TenderResponseDTO> updateTenderStatus(
            @Parameter(description = "ID of the tender to update")
            @PathVariable UUID tenderId,
            @Parameter(description = "New status for the tender")
            @RequestParam @Pattern(regexp = "CREATED|PUBLSHED|CLOSED|CANCELED", message = "Status has to be one of the four valid values") TenderStatus status) {
        return ResponseEntity.ok(tenderService.updateTenderStatus(tenderId, status));
    }

    @PatchMapping(value = "/{tenderId}/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Edit a tender",
            description = "Edit the details of an existing tender",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tender successfully edited", content = @Content(schema = @Schema(implementation = TenderResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Tender does not exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    public ResponseEntity<TenderResponseDTO> editTender(
            @Parameter(description = "ID of the tender to edit")
            @PathVariable UUID tenderId,
            @RequestBody TenderRequestDTO request) {
        return ResponseEntity.ok(tenderService.editTender(tenderId, request));
    }

    @PutMapping(value = "/{tenderId}/rollback/{version}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rollback a tender",
            description = "Rollback a tender to a previous version",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tender successfully rolled back to previous version", content = @Content(schema = @Schema(implementation = TenderResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Tender does not exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<TenderResponseDTO> rollbackTender(
            @Parameter(description = "ID of the tender to rollback")
            @PathVariable UUID tenderId,
            @Parameter(description = "Version to rollback to")
            @PathVariable long version) {
        return ResponseEntity.ok(tenderService.rollbackTender(tenderId, version));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Unauthorized", ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("Forbidden", ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Notfound", ex.getMessage()));
    }
}
