package coursework.backend.dto;

import coursework.backend.entity.enums.AuthorType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

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

    @NotNull(message = "предположительные сроки выполнения")
    private LocalDateTime expiredAt;


}
