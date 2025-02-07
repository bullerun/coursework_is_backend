package coursework.backend.service;

import coursework.backend.dto.BidRequestCreate;
import coursework.backend.dto.BidResponseDTO;
import coursework.backend.dto.mapper.BidMapper;
import coursework.backend.entity.Bid;
import coursework.backend.entity.enums.BidStatus;
import coursework.backend.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final UserService userService;


    @Transactional
    public BidResponseDTO createBid(BidRequestCreate request) {
        Bid bid = new Bid();
        bid.setName(request.getName());
        bid.setDescription(request.getDescription());
        bid.setTenderID(request.getTenderId());
        bid.setAuthorType(request.getAuthorType());
        bid.setAuthorId(request.getAuthorId());
        bid = bidRepository.save(bid);
        return BidMapper.toDto(bid);
    }

    public List<BidResponseDTO> getUserBids() {
        return (bidRepository.findAll()).stream().map(BidMapper::toDto)
                .toList();
    }

    public List<BidResponseDTO> getBidsForTender(UUID tenderId, int page, int pageSize) {
        return bidRepository.getBidsForTender(tenderId, BidStatus.PUBLISHED, userService.getCurrentUserUsername(), Page.page(pageSize, page))
                .stream()
                .map(BidMapper::toDto)
                .toList();
    }

}
