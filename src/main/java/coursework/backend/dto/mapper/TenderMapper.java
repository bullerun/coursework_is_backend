package coursework.backend.dto.mapper;

import coursework.backend.dto.TenderResponseDTO;
import coursework.backend.entity.Tender;
import org.springframework.stereotype.Component;

@Component
public class TenderMapper {

    public static TenderResponseDTO toDto(Tender tender) {
        return TenderResponseDTO.builder()
                .id(tender.getId())
                .name(tender.getName())
                .description(tender.getDescription())
                .cost(tender.getCost())
                .region(tender.getRegion().strip())
                .organizationId(tender.getOrganizationID())
                .ownerID(tender.getOwnerID())
                .build();
    }
}
