package coursework.backend.controller;

import coursework.backend.dto.BidRequestCreate;
import coursework.backend.dto.BidResponseDTO;
import coursework.backend.service.BidService;
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
}