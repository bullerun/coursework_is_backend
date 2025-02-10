package coursework.backend.dto.organization;

import coursework.backend.dto.OrganizationInvitesResponse;
import coursework.backend.entity.OrganisationInvite;

public class OrganizationMapper {
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
