package coursework.backend.dto.mapper;

import coursework.backend.dto.BidResponseDTO;
import coursework.backend.entity.Bid;

public class BidMapper {
    private BidMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static BidResponseDTO toDto(Bid bid) {
        return BidResponseDTO.builder()
                .id(bid.getId())
                .name(bid.getName())
                .bidStatus(bid.getBidStatus())
                .description(bid.getDescription())
                .tenderId(bid.getTenderID())
                .authorType(bid.getAuthorType())
                .authorId(bid.getAuthorId()).build();
    }

}
