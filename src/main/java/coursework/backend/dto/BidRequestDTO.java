package coursework.backend.dto;

import coursework.backend.entity.AuthorType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BidRequestDTO {

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    @Size(min = 1, max = 1000)
    private String description;

    @NotNull
    private Long tenderId;

    @NotNull
    @Pattern(regexp = "(ORGANIZATION|EMPLOYEE)")
    private AuthorType authorType;

    @NotNull
    private Long authorId;
}
