package coursework.backend.dto.feedback;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class FeedbackRequestDTO {
    @NotNull
    private UUID bidId;

    @NotBlank
    private String description;

    @NotNull
    private UUID organizationId;
}
