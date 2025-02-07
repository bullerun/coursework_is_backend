package coursework.backend.dto.mapper;

import coursework.backend.dto.BidResponseDTO;
import coursework.backend.entity.Bid;
import org.springframework.stereotype.Component;

@Component
public class BidMapper {
    //    public static BidRequestDTO toDto(Bid bid) {
//        BidRequestDTO bidRequestDTO = new BidRequestDTO();
//        bidRequestDTO.setName(bid.getName());
//        bidRequestDTO.setDescription(bid.getDescription());
//        bid.setTenderID(bid.getTenderID());
//        bid.setAuthorType(bid.getAuthorType());
//        bid.setAuthorId(bid.getAuthorId());
//        return bidRequestDTO;
//    }
    public static BidResponseDTO toDto(Bid bid) {
        return BidResponseDTO.builder()
                .id(bid.getId())
                .name(bid.getName())
                .description(bid.getDescription())
                .tenderId(bid.getTenderID())
                .authorType(bid.getAuthorType())
                .authorId(bid.getAuthorId()).build();
    }

}
