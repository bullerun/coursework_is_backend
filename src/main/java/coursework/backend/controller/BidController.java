package coursework.backend.controller;

import coursework.backend.dto.bid.BidRequestCreate;
import coursework.backend.dto.bid.BidRequestEdit;
import coursework.backend.dto.bid.BidResponseDTO;
import coursework.backend.dto.ErrorResponse;
import coursework.backend.entity.enums.BidStatus;
import coursework.backend.service.BidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bid")
@RequiredArgsConstructor
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class BidController {

    private final BidService bidService;

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a new bid",
            description = "Creates a new bid and assigns a unique identifier and creation time",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bid created successfully.", content = @Content(schema = @Schema(implementation = BidResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Request's format or its parameters are invalid.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), //TODO
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<BidResponseDTO> createBid(@RequestBody @Valid BidRequestCreate request) {
        return ResponseEntity.ok(bidService.createBid(request));
    }

    @GetMapping(value = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get current user's Bids",
            description = "Get a list of the current user's Bids",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bids retrieved", content = @Content(schema = @Schema(implementation = BidResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<List<BidResponseDTO>> getUserBids() {
        return ResponseEntity.ok(bidService.getUserBids());
    }

    @GetMapping(value = "/{tenderId}/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get Bids for a Tender",
            description = "Get Bids that associate with a Tender with stated ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bids retrieved",content = @Content(schema = @Schema(implementation = BidResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<List<BidResponseDTO>> getBidsForTender(@PathVariable @Valid @NotNull UUID tenderId,
                                                                 @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "Page number can't be negative") Integer page,
                                                                 @RequestParam(defaultValue = "10", required = false) @Min(value = 1, message = "Page size should be positive") Integer pageSize) {
        List<BidResponseDTO> bids = bidService.getBidsForTender(tenderId, page, pageSize);
        return ResponseEntity.ok(bids);
    }

    @GetMapping(value = "/{bidId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get status of a Bid",
            description = "Get the status of a Bid with stated ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bid status retrieved",content = @Content(schema = @Schema(implementation = BidStatus.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<BidStatus> getBidsStatus(@PathVariable @Valid @NotNull UUID bidId) {
        return ResponseEntity.ok(bidService.getBidsStatus(bidId));
    }

    @PutMapping(value = "/{bidId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Edit Bid status",
            description = "Edit the status of a Bid with stated ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bid status updated", content = @Content(schema = @Schema(implementation = BidResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<BidResponseDTO> editBidsStatus(@PathVariable @Valid @NotNull UUID bidId,
                                                         @RequestParam @Valid BidStatus bidstatus) {
        return ResponseEntity.ok(bidService.editBidsStatus(bidId, bidstatus));
    }

    @PatchMapping(value = "/{bidId}/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Edit Bid",
            description = "Edit Bid with stated ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bid updated successfully.", content = @Content(schema = @Schema(implementation = BidResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Request's format or its parameters are invalid.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<BidResponseDTO> editBid(
            @Parameter(description = "Bid ID")
            @PathVariable @Valid @NotNull UUID bidId,
            @RequestBody @Valid BidRequestEdit bid
    ) {
        return ResponseEntity.ok(bidService.editBid(bidId, bid));
    }

    @PutMapping(value = "/{bidId}/rollback/{version}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Rollback bid version",
            description = "Rollback bid's parameters to a stated version. It counts as a new edit, thus the version is incremented.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bid rolled back successfully and the version was incremented.", content = @Content(schema = @Schema(implementation = BidResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Request's format or its parameters are invalid.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PutMapping("/{bidId}/rollback/{version}")
    public ResponseEntity<BidResponseDTO> rollbackBid(
            @Parameter(description = "Bid ID")
            @PathVariable @Valid @NotNull UUID bidId,

            @Parameter(description = "Version to rollback Bid to.")
            @PathVariable @Valid @Min(value = 1, message = "version can not be less than 1") Long version

    ) {
        return ResponseEntity.ok(bidService.rollbackBid(bidId, version));
    }

}