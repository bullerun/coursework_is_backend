package coursework.backend.dto.mapper;

import coursework.backend.dto.organization.OrganizationInvitesResponse;
import coursework.backend.entity.OrganisationInvite;

public class OrganizationMapper {
    private OrganizationMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static OrganizationInvitesResponse toOrganizationInvitesResponse(OrganisationInvite invite) {
        return OrganizationInvitesResponse.builder()
                .id(invite.getId())
                .receiverName(invite.getReceiver().getUsername())
                .senderName(invite.getSender().getUsername())
                .status(invite.getStatus())
                .createdAt(invite.getCreatedAt())
                .build();
    }
}
