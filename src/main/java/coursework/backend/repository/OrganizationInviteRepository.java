package coursework.backend.repository;

import coursework.backend.entity.OrganisationInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrganizationInviteRepository extends JpaRepository<OrganisationInvite, UUID> {
    List<OrganisationInvite> findByReceiverId(UUID receiverId);
    List<OrganisationInvite> findBySenderId(UUID receiverId);
}
