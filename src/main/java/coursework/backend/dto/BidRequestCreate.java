package coursework.backend.dto;

import coursework.backend.entity.AuthorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BidRequestCreate {
    @NotBlank(message = "Название предложения не может быть пустым")
    private String name;

    @NotBlank(message = "Описание предложения не может быть пустым")
    private String description;

    @NotNull(message = "Идентификатор тендера обязателен")
    private UUID tenderId;

    @NotNull(message = "Тип автора обязателен")
    private AuthorType authorType;

    @NotNull(message = "Идентификатор автора обязателен")
    private UUID authorId;
}
