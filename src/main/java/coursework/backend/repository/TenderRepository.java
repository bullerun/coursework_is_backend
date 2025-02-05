package coursework.backend.repository;


import coursework.backend.entity.Tender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenderRepository extends JpaRepository<Tender, UUID> {
}

