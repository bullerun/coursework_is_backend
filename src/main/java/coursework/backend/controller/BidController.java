package coursework.backend.controller;

import coursework.backend.dto.BidRequestCreate;
import coursework.backend.dto.BidResponseDTO;
import coursework.backend.dto.ErrorResponse;
import coursework.backend.entity.enums.BidStatus;
import coursework.backend.service.BidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bid")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;



    @PostMapping("/new")
    @Operation(
            summary = "Create a new bid",
            description = "Creates a new bid and assigns a unique identifier and creation time",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bid successfully created", content = @Content(schema = @Schema(implementation = BidResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "bad field", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), //TODO
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<BidResponseDTO> createBid(@RequestBody @Valid BidRequestCreate request) {
        return ResponseEntity.ok(bidService.createBid(request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BidResponseDTO>> getUserBids() {
        return ResponseEntity.ok(bidService.getUserBids());
    }

    @GetMapping("/{tenderId}/list")
    public ResponseEntity<List<BidResponseDTO>> getBidsForTender(@PathVariable UUID tenderId,
                                                                 @RequestParam(defaultValue = "0", required = false) int page,
                                                                 @RequestParam(defaultValue = "10", required = false) int pageSize) {
        List<BidResponseDTO> bids = bidService.getBidsForTender(tenderId, page, pageSize);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/{bidId}/status")
    public ResponseEntity<BidStatus> getBidsStatus(@PathVariable UUID bidId) {
        return ResponseEntity.ok(bidService.getBidsStatus(bidId));
    }

    @PutMapping("/{bidId}/status")
    public ResponseEntity<BidResponseDTO> editBidsStatus(@PathVariable UUID bidId,
                                                         @RequestParam BidStatus bidstatus) {
        return ResponseEntity.ok(bidService.editBidsStatus(bidId, bidstatus));
    }
}