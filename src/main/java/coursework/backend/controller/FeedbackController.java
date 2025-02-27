package coursework.backend.controller;

import coursework.backend.dto.ErrorResponse;
import coursework.backend.dto.feedback.FeedbackDecisionDTO;
import coursework.backend.dto.feedback.FeedbackRequestDTO;
import coursework.backend.dto.feedback.FeedbackResponseDTO;
import coursework.backend.service.BidService;
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
@RequestMapping("/feedback")
@RequiredArgsConstructor
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class FeedbackController {

    private final BidService bidService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Send feedback to organization's bid",
            description = "Add feedback to a specified bid of a specified organization",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Feedback added successfully",
                            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Bid or organization no found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<FeedbackResponseDTO> addFeedback(@Valid @RequestBody FeedbackRequestDTO request) {
        return ResponseEntity.ok(bidService.addFeedback(request));
    }

    @GetMapping(value = "/{bidId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get feedback for Bid",
            description = "Get a list of all feedback for a Bid with a specified ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of feedback entries retrieved",
                            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbacks(@PathVariable UUID bidId) {
        return ResponseEntity.ok(bidService.getFeedbacksForBid(bidId));
    }


    @GetMapping(value = "/{bidId}/approved", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get Bid's approved feedback",
            description = "Get a list of approved feedback entries for a Bid with a specified ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of feedback entries retrieved",
                            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }

    )
    public ResponseEntity<List<FeedbackResponseDTO>> getApprovedFeedbacks(@PathVariable UUID bidId) {
        return ResponseEntity.ok(bidService.getApprovedFeedbacks(bidId));
    }

    @PostMapping(value = "/{bidId}/decision", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Submit decision on feedback",
            description = "Approve or reject feedback for a given Bid ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Decision processed successfully",
                            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Bid or feedback not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or is invalid.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<String> submitDecision(@PathVariable UUID bidId, @Valid @RequestBody FeedbackDecisionDTO decision) {
        return ResponseEntity.ok(bidService.processFeedbackDecision(bidId, decision));
    }
}
