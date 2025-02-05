package coursework.backend.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TenderRequestDTO {

    private String name;
    private String description;
    private Double cost;
    private String region;
    private UUID organizationId;
}
