package coursework.backend.repository;

import coursework.backend.entity.OrganisationEmployee;
import coursework.backend.entity.OrganisationEmployeeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationEmployeeRepository extends JpaRepository<OrganisationEmployee, OrganisationEmployeeId> {

}
