package coursework.backend.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrganisationEmployeeId implements java.io.Serializable {
    private UUID organisationId;
    private UUID employeeId;
}
