package coursework.backend.repository;

import coursework.backend.entity.Feedback;
import coursework.backend.entity.enums.FeedbackStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findByBidId(UUID bidId);
    List<Feedback> findByBidIdAndFeedbackStatus(UUID bidId, FeedbackStatus status);
}
