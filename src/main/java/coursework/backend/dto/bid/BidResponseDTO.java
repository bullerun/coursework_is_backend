package coursework.backend.dto.bid;

import coursework.backend.entity.enums.AuthorType;
import coursework.backend.entity.enums.BidStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BidResponseDTO {
    @NotBlank
    private UUID id;
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    @Pattern(regexp = "(CREATED|PUBLISHED|CLOSED)")
    private BidStatus bidStatus;

    private Long version;

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

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;
}

