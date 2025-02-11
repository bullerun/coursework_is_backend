package coursework.backend.entity;

import coursework.backend.entity.enums.AuthorType;
import coursework.backend.entity.enums.BidStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "bid")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tender_id", nullable = false)
    @MapsId("tenderID")
    private Tender tender;

    @Column(name = "tender_id")
    private UUID tenderID;

    @Column(name = "cost", nullable = false)
    private Long cost;

    @Column(nullable = false)
    private String region;

    @Enumerated(EnumType.STRING)
    @Column(name = "author_type", nullable = false)
    private AuthorType authorType;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "version", nullable = false, columnDefinition = "BIGINT DEFAULT 1")
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "bid_status", nullable = false)
    private BidStatus bidStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @MapsId("ownerID")
    private User owner;

    @Column(name = "owner_id")
    private UUID ownerID;
}
