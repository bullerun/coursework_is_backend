package coursework.backend.dto.mapper;

import coursework.backend.dto.BidResponseDTO;
import coursework.backend.entity.Bid;
import coursework.backend.entity.BidHistory;

public class BidMapper {
    private BidMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static BidResponseDTO toDto(Bid bid) {
        return BidResponseDTO.builder()
                .id(bid.getId())
                .name(bid.getName())
                .bidStatus(bid.getBidStatus())
                .version(bid.getVersion())
                .description(bid.getDescription())
                .tenderId(bid.getTenderID())
                .authorType(bid.getAuthorType())
                .authorId(bid.getAuthorId())
                .createdAt(bid.getCreatedAt())
                .expiredAt(bid.getExpiredAt())
                .build();
    }


    public static void historyToEntity(Bid bid, BidHistory BidHistory) {
        bid.setName(BidHistory.getName());
        bid.setDescription(BidHistory.getDescription());
        bid.setCost(BidHistory.getCost());
        bid.setRegion(BidHistory.getRegion());
        bid.setExpiredAt(BidHistory.getExpiredAt());
        bid.setBidStatus(BidHistory.getBidStatus());
    }
}
