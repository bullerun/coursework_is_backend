package coursework.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BidDTO {

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    @Size(min = 1, max = 1000)
    private String description;

    @NotNull
    private Long tenderId;

    @NotNull
    @Pattern(regexp = "(COMPANY|INDIVIDUAL)")
    private String authorType;

    @NotNull
    private Long authorId;
}
