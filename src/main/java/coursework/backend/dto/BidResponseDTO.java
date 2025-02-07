package coursework.backend.dto;

import coursework.backend.entity.AuthorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BidResponseDTO {
    @NotBlank
    private UUID id;
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @NotBlank
    @Size(min = 1, max = 1000)
    private String description;

    @NotBlank
    private UUID tenderId;

    @NotNull
    @Pattern(regexp = "(ORGANIZATION|EMPLOYEE)")
    private AuthorType authorType;

    @NotNull
    private UUID authorId;
}

