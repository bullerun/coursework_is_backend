package coursework.backend.entity;

import coursework.backend.entity.enums.TenderStatus;
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
@Table(name = "tender_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenderHistory {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "cost", nullable = false)
    private Long cost;

    @Column(name = "region", nullable = false)
    private String region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    @MapsId("organizationID")
    private Organization organization;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationID;

    @Column(name = "version", nullable = false, columnDefinition = "BIGINT DEFAULT 1")
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "tender_status", nullable = false)
    private TenderStatus tenderStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

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
