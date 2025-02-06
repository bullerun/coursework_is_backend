package coursework.backend.repository;

import coursework.backend.entity.OrganisationEmployee;
import coursework.backend.entity.OrganisationEmployeeId;
import coursework.backend.entity.Organization;
import coursework.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganisationEmployeeRepository extends JpaRepository<OrganisationEmployee, OrganisationEmployeeId> {

    // Получить всех сотрудников организации
    List<OrganisationEmployee> findByOrganisation(Organization organisation);

    // Получить всех сотрудников организации по ID
    List<OrganisationEmployee> findByOrganisation_Id(UUID organisationId);

    // Получить все организации, в которых работает пользователь
    List<OrganisationEmployee> findByEmployee(User employee);

    // Получить сотрудника организации по ID организации и ID пользователя
    Optional<OrganisationEmployee> findByOrganisation_IdAndEmployee_Id(UUID organisationId, UUID employeeId);
}
