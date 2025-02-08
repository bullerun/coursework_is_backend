package coursework.backend.repository;


import coursework.backend.entity.Tender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenderRepository extends JpaRepository<Tender, UUID> {

    @Query("SELECT t FROM Tender t JOIN t.owner o WHERE o.username = :username")
    List<Tender> getTenderByUsersUsername(@Param("username") String username);

    Optional<Tender> getTenderById(UUID tenderId);
}
