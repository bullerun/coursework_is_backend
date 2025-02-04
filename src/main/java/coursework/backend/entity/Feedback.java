package coursework.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "bind_id")
    private Bid bid;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organization organization;

    @Enumerated(EnumType.STRING)
    private FeedbackStatus feedbackStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
