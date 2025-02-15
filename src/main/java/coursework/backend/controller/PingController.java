package coursework.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @GetMapping(value = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Ping",
            description = "Ping the server"
    )
    @ApiResponse(responseCode = "200", description = "Server is up",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }
}
