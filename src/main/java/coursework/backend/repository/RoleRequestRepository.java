package coursework.backend.repository;

import coursework.backend.entity.RoleRequest;
import coursework.backend.entity.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRequestRepository extends JpaRepository<RoleRequest, UUID> {

    List<RoleRequest> findByStatus(RequestStatus status);

    List<RoleRequest> findByUserIdAndStatus(UUID userId, RequestStatus status);

    boolean existsByUserIdAndStatus(UUID userId, RequestStatus status);
}