package coursework.backend.repository;

import coursework.backend.entity.Bid;
import coursework.backend.entity.enums.AuthorType;
import coursework.backend.entity.enums.BidStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {

    @Query("SELECT b FROM Bid b " +
            "WHERE b.tenderID = :tenderId " +
            "AND (b.bidStatus = :bidStatus OR " +
            "b.authorId IN (SELECT o.organisation.id FROM OrganisationEmployee o WHERE o.employee.username = :username))")
    List<Bid> getBidsForTender(
            @Param("tenderId") UUID tenderId,
            @Param("bidStatus") BidStatus bidStatus,
            @Param("username") String username,
            Pageable pageable
    );

    @Query("SELECT b FROM Bid b JOIN b.owner u " +
            "WHERE u.username = :username OR " +
            "(b.authorType = :authorType AND " +
            "b.authorId IN (SELECT o.organisation.id FROM OrganisationEmployee o WHERE o.employee.username = :username))")
    List<Bid> getUserBids(@Param("username") String username, @Param("authorType") AuthorType authorType);

//    @Query("SELECT COUNT(b) > 0 FROM Bid b " +
//            "WHERE b.authorType = :authorType AND " +
//            "b.authorId IN (SELECT o.organisation.id FROM OrganisationEmployee o WHERE o.employee.username = :username)")
//    Optional<Bid> getBidById(UUID bidId);

    @Query("""
                SELECT b FROM Bid b
                WHERE b.id = :bidId
                AND (
                    (b.authorType = 'EMPLOYEE' AND b.ownerID = :userId)
                    OR (b.authorType = 'ORGANIZATION' AND EXISTS (
                        SELECT 1 FROM OrganisationEmployee uo
                                    JOIN uo.employee u
                                    JOIN uo.organisation o
                                    WHERE u.id = :userId AND o.id = b.authorId
                    ))
                )
            """)
    Optional<Bid> findBidWithPermission(@Param("bidId") UUID bidId, @Param("userId") UUID userId);

    List<Bid> findByTenderIdAndIdNot(UUID id, UUID bidId);
}
