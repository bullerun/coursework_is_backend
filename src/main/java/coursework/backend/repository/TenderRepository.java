package coursework.backend.repository;


import coursework.backend.entity.Tender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenderRepository extends JpaRepository<Tender, UUID> {
    @Query("select t FROM Tender t " +
            "join User u on u.id = t.ownerID where u.username = :username")
    List<Tender> getTenderByUsersUsername(@Param("username") String username);

    Optional<Tender> getTenderById(UUID tenderId);
}

