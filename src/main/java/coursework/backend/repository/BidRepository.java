package coursework.backend.repository;

import coursework.backend.entity.Bid;
import coursework.backend.entity.enums.AuthorType;
import coursework.backend.entity.enums.BidStatus;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {
    @Query("select b from Bid b where b.tenderID = :tenderId and (b.bidStatus = :bidStatus or b.authorId in (select o.organisation.id From OrganisationEmployee o where o.employee.username = :username))")
    List<Bid> getBidsForTender(@PathVariable("tenderId") UUID tenderId, @PathVariable("bidStatus") BidStatus bidStatus, @PathVariable("username") String username, Page page);

    @Query("select b from Bid b where b.id = :id")
    Optional<Bid> getBidsById(@PathVariable("id") UUID id);

    @Query("select b from Bid b join b.owner u where u.username = :username or (b.authorType = :authorType and b.authorId in (select o.organisation.id From OrganisationEmployee o where o.employee.username = :username))")
    List<Bid> getUserBids(String username, AuthorType authorType);

    @Query("SELECT COUNT(b) > 0 from Bid b where b.authorType = :authorType and b.authorId in (select o.organisation.id From OrganisationEmployee o where o.employee.username = :username)")
    boolean checkUserPermissionsIfAuthorIsOrganization(@Param("username") String username, AuthorType authorType);
}
