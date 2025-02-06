package coursework.backend.controller;

import coursework.backend.dto.ErrorResponse;
import coursework.backend.dto.TenderRequestDTO;
import coursework.backend.entity.Tender;
import coursework.backend.entity.TenderStatus;
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

    @GetMapping("/ping")
    @Operation(summary = "Ping the server", description = "Checks if the server is up and running")
    @ApiResponse(responseCode = "200", description = "Server is up")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("ok");
    }

//    @GetMapping
//    @Operation(summary = "Get all tenders", description = "Retrieve a list of tenders, optionally filtered by service type")
//    @ApiResponse(responseCode = "200", description = "List of tenders retrieved")
//    public ResponseEntity<List<Tender>> getTenders(
//            @Parameter(description = "List of service types to filter the tenders")
//            @RequestParam(required = false) List<String> serviceType) {
//        return ResponseEntity.ok(tenderService.getTenders(serviceType));
//    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a new tender",
            description = "Creates a new tender and assigns a unique identifier and creation time",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tender successfully created", content = @Content(schema = @Schema(implementation = Tender.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<Tender> createTender(@RequestBody @Valid TenderRequestDTO request) {
        return ResponseEntity.ok(tenderService.createTender(request));
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

    @GetMapping("/my")
    @Operation(summary = "Get user tenders", description = "Retrieve tenders associated with a specific user")
    @ApiResponse(responseCode = "200", description = "List of user tenders retrieved")
    public ResponseEntity<List<Tender>> getUserTenders() {
        return ResponseEntity.ok(tenderService.getUserTenders());
    }

    @GetMapping("/{tenderId}/status")
    @Operation(summary = "Get tender status", description = "Retrieve the current status of a specific tender")
    @ApiResponse(responseCode = "200", description = "Tender status retrieved")
    public ResponseEntity<String> getTenderStatus(
            @Parameter(description = "ID of the tender to retrieve status for")
            @PathVariable UUID tenderId) {
        return ResponseEntity.ok(tenderService.getTenderStatus(tenderId));
    }

    @PutMapping("/{tenderId}/status")
    @Operation(summary = "Update tender status", description = "Update the status of a specific tender")
    @ApiResponse(responseCode = "200", description = "Tender status successfully updated")
    public ResponseEntity<Tender> updateTenderStatus(
            @Parameter(description = "ID of the tender to update")
            @PathVariable UUID tenderId,
            @Parameter(description = "New status for the tender")
            @RequestParam TenderStatus status) {
        return ResponseEntity.ok(tenderService.updateTenderStatus(tenderId, status));
    }

    @PatchMapping("/{tenderId}/edit")
    @Operation(summary = "Edit a tender", description = "Edit the details of an existing tender")
    @ApiResponse(responseCode = "200", description = "Tender successfully edited")
    public ResponseEntity<Tender> editTender(
            @Parameter(description = "ID of the tender to edit")
            @PathVariable UUID tenderId,
            @RequestBody TenderRequestDTO request) {
        return ResponseEntity.ok(tenderService.editTender(tenderId, request));
    }

    @PutMapping("/{tenderId}/rollback/{version}")
    @Operation(summary = "Rollback a tender", description = "Rollback a tender to a previous version")
    @ApiResponse(responseCode = "200", description = "Tender successfully rolled back to previous version")
    public ResponseEntity<Tender> rollbackTender(
            @Parameter(description = "ID of the tender to rollback")
            @PathVariable UUID tenderId,
            @Parameter(description = "Version to rollback to")
            @PathVariable long version) {
        return ResponseEntity.ok(tenderService.rollbackTender(tenderId, version));
    }
}
