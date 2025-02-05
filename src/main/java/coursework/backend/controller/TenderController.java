package coursework.backend.controller;

import coursework.backend.dto.TenderRequestDTO;
import coursework.backend.entity.Tender;
import coursework.backend.service.TenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/tenders")
@RequiredArgsConstructor
@Tag(name = "Tender Management", description = "API for managing tenders")
public class TenderController {

    private final TenderService tenderService;


    @PostMapping("/new")
    @Operation(summary = "Create a new tender", description = "Create a new tender from the provided request data")
    @ApiResponse(responseCode = "200", description = "Tender successfully created")
    public ResponseEntity<Tender> createTender(@RequestBody TenderRequestDTO request) {
        return ResponseEntity.ok(tenderService.createTender(request));
    }
}
