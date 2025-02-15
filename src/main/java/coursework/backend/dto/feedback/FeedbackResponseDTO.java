package coursework.backend.dto.feedback;

import coursework.backend.entity.enums.FeedbackStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FeedbackResponseDTO {
    private UUID id;
    private String description;
    private FeedbackStatus feedbackStatus;
    private LocalDateTime createdAt = LocalDateTime.now();
}
