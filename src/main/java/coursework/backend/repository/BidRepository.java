package coursework.backend.repository;

import coursework.backend.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface BidRepository extends JpaRepository<Bid, UUID> {

}
