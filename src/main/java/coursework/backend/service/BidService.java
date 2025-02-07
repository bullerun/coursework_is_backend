package coursework.backend.service;

import coursework.backend.dto.BidRequestCreate;
import coursework.backend.dto.BidResponseDTO;
import coursework.backend.dto.mapper.BidMapper;
import coursework.backend.entity.Bid;
import coursework.backend.entity.enums.AuthorType;
import coursework.backend.entity.enums.BidStatus;
import coursework.backend.exception.ForbiddenException;
import coursework.backend.exception.NotFoundException;
import coursework.backend.repository.BidRepository;
import coursework.backend.repository.UserRepository;
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
    private final UserRepository userRepository;


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

    public BidStatus getBidsStatus(UUID bidId) {
        // TODO проверка на то что предложение не скрыт или если он скрыт то пользователь имеет право просматривать
        return bidRepository.getBidsById(bidId).orElseThrow(
                () -> new NotFoundException("Could not find bid with id: " + bidId)
        ).getBidStatus();
    }

    public BidResponseDTO editBidsStatus(UUID bidId, BidStatus bidStatus) {
        var bid = bidRepository.getBidsById(bidId).orElseThrow(
                () -> new NotFoundException("Could not find bid with id: " + bidId)
        );
        if (bid.getAuthorType() == AuthorType.EMPLOYEE && bid.getAuthorId() != userService.getCurrentUser().getId() ||
                bid.getAuthorType() == AuthorType.ORGANIZATION && userRepository.invertExistsByUserAndOrganization(userService.getCurrentUserUsername(), bid.getAuthorId())) {
            throw new ForbiddenException("permission denied");
        }
        bid.setBidStatus(bidStatus);
        return BidMapper.toDto(bidRepository.save(bid));
    }
}
