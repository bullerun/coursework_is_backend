package coursework.backend.dto.organization;

import lombok.Data;

import java.util.UUID;

@Data
public class InviteRequest {
    private String receiverUsername;
    private UUID organizationId;
}
