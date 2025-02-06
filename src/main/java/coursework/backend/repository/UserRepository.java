package coursework.backend.repository;

import coursework.backend.entity.EmployeePositionInOrganization;
import coursework.backend.entity.Role;
import coursework.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);


    @Query("SELECT COUNT(uo) > 0 FROM OrganisationEmployee uo " +
            "JOIN uo.employee u " +
            "JOIN uo.organisation o " +
            "WHERE u.username = :username AND o.id = :organizationUuid")
    boolean existsByUserAndOrganization(@Param("username") String username,
                                        @Param("organizationUuid") UUID organizationUuid);



    @Query("SELECT COUNT(uo) > 0 FROM OrganisationEmployee uo " +
            "JOIN uo.employee u " +
            "JOIN uo.organisation o " +
            "WHERE u.username = :username AND o.id = :organizationUuid and uo.position = :position")
    boolean existsByUserAndOrganizationAndHasProvide(@Param("username") String username,
                                                     @Param("organizationUuid") UUID organizationUuid, @Param("position")EmployeePositionInOrganization position);
    @Modifying
    @Query("update User u set u.role = :role where u.id = :id")
    void updateRole(@Param("id") Long id, @Param("role") Role role);

}