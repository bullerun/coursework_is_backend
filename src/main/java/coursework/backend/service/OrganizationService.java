package coursework.backend.service;

import coursework.backend.dto.OrganizationInvitesResponse;
import coursework.backend.dto.organization.InviteRequest;
import coursework.backend.dto.organization.OrganizationMapper;
import coursework.backend.dto.organization.OrganizationRequestDTO;
import coursework.backend.entity.*;
import coursework.backend.entity.enums.EmployeePositionInOrganization;
import coursework.backend.entity.enums.InviteStatus;
import coursework.backend.exception.NotFoundException;
import coursework.backend.repository.OrganisationEmployeeRepository;
import coursework.backend.repository.OrganizationInviteRepository;
import coursework.backend.repository.OrganizationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserService userService;
    private final OrganisationEmployeeRepository organisationEmployeeRepository;
    private final OrganizationInviteRepository inviteRepository;

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

    @Transactional
    public void createOrganizationInvite(InviteRequest request) {
        User sender = userService.getCurrentUser();
        User receiver = userService.findByUsername(request.getReceiverUsername());
        OrganisationInvite organisationInvite = OrganisationInvite.builder()
                .sender(sender)
                .receiver(receiver)
                .organizationId(request.getOrganizationId())
                .build();
        inviteRepository.save(organisationInvite);
    }

    public OrganisationInvite getInviteById(UUID id) {
        OrganisationInvite invite = inviteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invite not found"));

        if (!invite.getStatus().equals(InviteStatus.PENDING)) {
            throw new IllegalArgumentException("Invite status is not PENDING");
        }
        return invite;
    }

    @Transactional
    public void acceptInvite(UUID inviteId) {
        OrganisationInvite invite = getInviteById(inviteId);
        invite.setStatus(InviteStatus.ACCEPTED);
        inviteRepository.save(invite);

        Organization organization = getOrganizationById(invite.getOrganizationId());
        // Логика добавления пользователя в организацию
        OrganisationEmployee organisationEmployee = OrganisationEmployee.builder()
                .id(new OrganisationEmployeeId(organization.getId(), invite.getReceiver().getId()))
                .employee(invite.getReceiver()).organisation(organization)
                .position(EmployeePositionInOrganization.WORKER)
                .build();
        organisationEmployeeRepository.save(organisationEmployee);
    }

    @Transactional
    public void declineInvite(UUID inviteId) {
        OrganisationInvite invite = getInviteById(inviteId);
        invite.setStatus(InviteStatus.DECLINED);
        inviteRepository.save(invite);
    }

    public List<OrganizationInvitesResponse> getMyInvitations() {
        return inviteRepository.findByReceiverId(userService.getCurrentUser().getId()).stream().map(OrganizationMapper::toOrganizationInvitesResponse).toList();
    }

    public List<OrganizationInvitesResponse> getMyInvites() {
        return inviteRepository.findBySenderId(userService.getCurrentUser().getId()).stream().map(OrganizationMapper::toOrganizationInvitesResponse).toList();
    }

    public Organization getOrganizationById(UUID id) {
        return organizationRepository.findById(id).orElseThrow(() -> new NotFoundException("Organization not found"));
    }

    //TODO сделать повышение прав
}
