package coursework.backend.repository;

import coursework.backend.entity.BidHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BidHistoryRepository extends JpaRepository<BidHistory, UUID> {
    Optional<BidHistory> findByBidIdAndVersion(UUID id, Long version);
}
