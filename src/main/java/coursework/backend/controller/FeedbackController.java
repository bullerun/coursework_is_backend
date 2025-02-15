package coursework.backend.controller;

import coursework.backend.dto.feedback.FeedbackRequestDTO;
import coursework.backend.dto.feedback.FeedbackResponseDTO;
import coursework.backend.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final BidService bidService;

    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> addFeedback(@Valid @RequestBody FeedbackRequestDTO request) {
        return ResponseEntity.ok(bidService.addFeedback(request));
    }

    @GetMapping("/{bidId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbacks(@PathVariable UUID bidId) {
        return ResponseEntity.ok(bidService.getFeedbacksForBid(bidId));
    }

    @GetMapping("/{bidId}/approved")
    public ResponseEntity<List<FeedbackResponseDTO>> getApprovedFeedbacks(@PathVariable UUID bidId) {
        return ResponseEntity.ok(bidService.getApprovedFeedbacks(bidId));
    }
}
