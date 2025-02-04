package coursework.backend.entity;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "organisation_employee")
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrganisationEmployee {
    @EmbeddedId
    private OrganisationEmployeeId id;

    @ManyToOne
    @MapsId("organisationId")
    @JoinColumn(name = "organisation_id", nullable = false, foreignKey = @ForeignKey(name = "fk_organisation_employee_organisation"))
    private Organization organisation;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id", nullable = false, foreignKey = @ForeignKey(name = "fk_organisation_employee_employee"))
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    private EmployeePositionInOrganization position;
}