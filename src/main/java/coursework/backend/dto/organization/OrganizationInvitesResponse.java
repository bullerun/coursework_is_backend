package coursework.backend.dto.organization;

import coursework.backend.entity.enums.InviteStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrganizationInvitesResponse {
    private final UUID id;
    private final String senderName;
    private final String receiverName;
    private final LocalDateTime createdAt;
    private final InviteStatus status;
}
