package coursework.backend.repository;

import coursework.backend.entity.OrganisationEmployee;
import coursework.backend.entity.OrganisationEmployeeId;
import coursework.backend.entity.enums.EmployeePositionInOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganisationEmployeeRepository extends JpaRepository<OrganisationEmployee, OrganisationEmployeeId> {
    @Query("SELECT ou FROM OrganisationEmployee ou  where ou.employee.username = :username and ou.organisation.id = :id")
    Optional<OrganisationEmployee> findByOrganisationIdAndEmployeeUsername(@Param("id") UUID id, @Param("username") String username);

    @Modifying
    @Query("UPDATE OrganisationEmployee ou SET ou.position = :position WHERE ou.employee.username = :username AND ou.organisation.id = :id")
    int updatePositionByOrganisationIdAndEmployeeUsername(@Param("id") UUID id, @Param("username") String username, @Param("position") EmployeePositionInOrganization position);

}
