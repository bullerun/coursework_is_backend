package coursework.backend.dto.mapper;

import coursework.backend.dto.FeedbackResponseDTO;
import coursework.backend.entity.Feedback;

public class FeedbackMapper {
    public static FeedbackResponseDTO toDto(Feedback feedback) {
        FeedbackResponseDTO dto = new FeedbackResponseDTO();
        dto.setId(feedback.getId());
        dto.setDescription(feedback.getDescription());
        dto.setFeedbackStatus(feedback.getFeedbackStatus());
        dto.setCreatedAt(feedback.getCreatedAt());
        return dto;
    }
}
