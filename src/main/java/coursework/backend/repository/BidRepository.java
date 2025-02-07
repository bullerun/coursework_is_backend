package coursework.backend.repository;

import coursework.backend.entity.Bid;
import coursework.backend.entity.enums.BidStatus;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {
    @Query("select b from Bid b where b.tenderID = :tenderId and (b.bidStatus = :bidStatus or b.authorId in (select o.organisation.id From OrganisationEmployee o where o.employee.username = :username))")
    List<Bid> getBidsForTender(@PathVariable("tenderId") UUID tenderId, @PathVariable("bidStatus") BidStatus bidStatus, @PathVariable("username") String username, Page page);
}
