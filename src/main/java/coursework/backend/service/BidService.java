package coursework.backend.service;

import coursework.backend.dto.BidRequestCreate;
import coursework.backend.dto.BidResponseDTO;
import coursework.backend.dto.mapper.BidMapper;
import coursework.backend.entity.Bid;
import coursework.backend.entity.BidStatus;
import coursework.backend.repository.BidRepository;
import coursework.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
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
//        return bidRepository.findByUsername(userService.getCurrentUserUsername())
//                .stream()
//                .map(BidMapper::toDto)
//                .toList();
        return (bidRepository.findAll()).stream().map(BidMapper::toDto)
                .toList();
    }

    public List<BidResponseDTO> getBidsForTender(UUID tenderId, int page, int pageSize) {
//        Page.page(pageSize,page)
        return bidRepository.getBidsForTender(tenderId, BidStatus.PUBLISHED, userService.getCurrentUserUsername())
                .stream()
                .map(BidMapper::toDto)
                .toList();
    }

//    public List<BidDTO> getBidsForTender(UUID tenderId, String username, int limit, int offset) {
//        return bidRepository.findByTenderId(tenderId, username, limit, offset)
//                .stream()
//                .map(BidMapper::toDto)
//                .toList();
//    }
//
//    /**
//     * Получение текущего статуса предложения.
//     */
//    public Optional<BidStatus> getBidStatus(UUID bidId) {
//        return bidRepository.findById(bidId).map(Bid::getBidStatus);
//    }
//
//    /**
//     * Обновление статуса предложения.
//     */
//    @Transactional
//    public BidDTO updateBidStatus(UUID bidId, BidStatus status) {
//        Bid bid = bidRepository.findById(bidId)
//                .orElseThrow(() -> new NotFoundException("Bid not found"));
//        bid.setBidStatus(status);
//        bid = bidRepository.save(bid);
//        return BidMapper.toDto(bid);
//    }
//
//    /**
//     * Редактирование предложения.
//     */
//    @Transactional
//    public BidDTO editBid(UUID bidId, BidRequestEdit request) {
//        Bid bid = bidRepository.findById(bidId)
//                .orElseThrow(() -> new NotFoundException("Bid not found"));
//
//        if (request.getName() != null) {
//            bid.setName(request.getName());
//        }
//        if (request.getDescription() != null) {
//            bid.setDescription(request.getDescription());
//        }
//
//        bid = bidRepository.save(bid);
//        return BidMapper.toDto(bid);
//    }
}
