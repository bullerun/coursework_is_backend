package coursework.backend.dto.mapper;

import coursework.backend.dto.BidDTO;
import coursework.backend.entity.Bid;
import org.springframework.stereotype.Component;

@Component
public class BidMapper {
    public static BidDTO toDto(Bid bid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setName(bid.getName());
        bidDTO.setDescription(bid.getDescription());
        bid.setTenderID(bid.getTenderID());
        bid.setAuthorType(bid.getAuthorType());
        bid.setAuthorId(bid.getAuthorId());
        return bidDTO;
    }

}
