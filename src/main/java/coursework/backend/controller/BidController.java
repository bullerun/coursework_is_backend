package coursework.backend.controller;

import coursework.backend.dto.BidRequestCreate;
import coursework.backend.dto.BidRequestEdit;
import coursework.backend.dto.BidResponseDTO;
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
    public ResponseEntity<List<BidResponseDTO>> getBidsForTender(@PathVariable @Valid @NotNull UUID tenderId,
                                                                 @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "Page number can't be negative") Integer page,
                                                                 @RequestParam(defaultValue = "10", required = false) @Min(value = 1, message = "Page size should be positive") Integer pageSize) {
        List<BidResponseDTO> bids = bidService.getBidsForTender(tenderId, page, pageSize);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/{bidId}/status")
    public ResponseEntity<BidStatus> getBidsStatus(@PathVariable @Valid @NotNull UUID bidId) {
        return ResponseEntity.ok(bidService.getBidsStatus(bidId));
    }

    @PutMapping("/{bidId}/status")
    public ResponseEntity<BidResponseDTO> editBidsStatus(@PathVariable @Valid @NotNull UUID bidId,
                                                         @RequestParam @Valid BidStatus bidstatus) {
        return ResponseEntity.ok(bidService.editBidsStatus(bidId, bidstatus));
    }

    @Operation(
            summary = "Edit bid",
            description = "Edit bid",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Предложение успешно обновлено"),
                    @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
                    @ApiResponse(responseCode = "404", description = "Предложение не найдено")
            }
    )
    @PatchMapping("/{bidId}/edit")
    public ResponseEntity<BidResponseDTO> editBid(
            @Parameter(description = "Идентификатор предложения")
            @PathVariable @Valid @NotNull UUID bidId,
            @RequestBody @Valid BidRequestEdit bid
    ) {
        return ResponseEntity.ok(bidService.editBid(bidId, bid));
    }

    @Operation(
            summary = "Rollback bid version",
            description = "Откатить параметры предложения к указанной версии. Это считается новой правкой, поэтому версия инкрементируется.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Предложение успешно откатано и версия инкрементирована."),
                    @ApiResponse(responseCode = "400", description = "Неверный формат запроса или его параметры."),
                    @ApiResponse(responseCode = "401", description = "Пользователь не существует или некорректен."),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав для выполнения действия."),
                    @ApiResponse(responseCode = "404", description = "Предложение или версия не найдены.")
            }
    )
    @PutMapping("/{bidId}/rollback/{version}")
    public ResponseEntity<BidResponseDTO> rollbackBid(
            @Parameter(description = "Идентификатор предложения")
            @PathVariable @Valid @NotNull UUID bidId,

            @Parameter(description = "Номер версии, к которой нужно откатить предложение.")
            @PathVariable @Valid @Min(value = 1, message = "версия не может быть меньше 1") Long version

    ) {
        return ResponseEntity.ok(bidService.rollbackBid(bidId, version));
    }

}