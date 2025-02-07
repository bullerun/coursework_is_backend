package coursework.backend.service;

import coursework.backend.dto.OrganizationRequestDTO;
import coursework.backend.entity.*;
import coursework.backend.entity.enums.EmployeePositionInOrganization;
import coursework.backend.repository.OrganisationEmployeeRepository;
import coursework.backend.repository.OrganizationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserService userService;
    private final OrganisationEmployeeRepository organisationEmployeeRepository;

    @Transactional
    public Organization createOrganization(@Valid OrganizationRequestDTO request) {
        Organization organization = Organization.builder().name(request.getName()).description(request.getDescription()).build();
        organization = organizationRepository.save(organization);

        User user = userService.getCurrentUser();

        OrganisationEmployee organisationEmployee = OrganisationEmployee.builder()
                .id(new OrganisationEmployeeId(organization.getId(), user.getId()))
                .employee(user).organisation(organization)
                .position(EmployeePositionInOrganization.HEAD)
                .build();
        organisationEmployeeRepository.save(organisationEmployee);
        return organization;
    }
}
