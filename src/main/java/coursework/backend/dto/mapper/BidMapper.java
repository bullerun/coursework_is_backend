package coursework.backend.dto.mapper;

import coursework.backend.dto.bid.BidResponseDTO;
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


    public static void historyToEntity(Bid bid, BidHistory bidHistory) {
        bid.setName(bidHistory.getName());
        bid.setDescription(bidHistory.getDescription());
        bid.setCost(bidHistory.getCost());
        bid.setRegion(bidHistory.getRegion());
        bid.setExpiredAt(bidHistory.getExpiredAt());
        bid.setBidStatus(bidHistory.getBidStatus());
    }
}
