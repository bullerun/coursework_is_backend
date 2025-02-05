package coursework.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BidRequestCreate {
    @NotBlank(message = "Название предложения не может быть пустым")
    private String name;

    @NotBlank(message = "Описание предложения не может быть пустым")
    private String description;

    @NotNull(message = "Идентификатор тендера обязателен")
    private Long tenderId;

    @NotNull(message = "Тип автора обязателен")
    private String authorType;

    @NotNull(message = "Идентификатор автора обязателен")
    private Long authorId;
}
