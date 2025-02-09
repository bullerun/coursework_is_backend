package coursework.backend.repository;

import coursework.backend.entity.TenderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenderHistoryRepository extends JpaRepository<TenderHistory, UUID> {
    Optional<TenderHistory> findByTenderIdAndVersion(UUID id, Long version);
}
