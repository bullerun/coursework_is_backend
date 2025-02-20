package coursework.backend.dto.mapper;

import coursework.backend.dto.tender.TenderResponseDTO;
import coursework.backend.entity.Tender;
import coursework.backend.entity.TenderHistory;

public class TenderMapper {
    private TenderMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static TenderResponseDTO toDto(Tender tender) {
        return TenderResponseDTO.builder()
                .id(tender.getId())
                .name(tender.getName())
                .description(tender.getDescription())
                .cost(tender.getCost())
                .region(tender.getRegion().strip())
                .organizationId(tender.getOrganizationID())
                .ownerID(tender.getOwnerID())
                .expiredAt(tender.getExpiredAt())
                .build();
    }

    public static void historyToEntity(Tender tender, TenderHistory tenderHistory) {
        tender.setName(tenderHistory.getName());
        tender.setDescription(tenderHistory.getDescription());
        tender.setCost(tenderHistory.getCost());
        tender.setRegion(tenderHistory.getRegion());
        tender.setTenderStatus(tenderHistory.getTenderStatus());
        tender.setExpiredAt(tenderHistory.getExpiredAt());
    }
}
