package coursework.backend.service;

import coursework.backend.dto.BidRequestCreate;
import coursework.backend.dto.BidRequestEdit;
import coursework.backend.dto.BidResponseDTO;
import coursework.backend.dto.mapper.BidMapper;
import coursework.backend.entity.Bid;
import coursework.backend.entity.BidHistory;
import coursework.backend.entity.enums.AuthorType;
import coursework.backend.entity.enums.BidStatus;
import coursework.backend.exception.ForbiddenException;
import coursework.backend.exception.NotFoundException;
import coursework.backend.repository.BidHistoryRepository;
import coursework.backend.repository.BidRepository;
import coursework.backend.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    private final BidHistoryRepository bidHistoryRepository;


    @Transactional
    public BidResponseDTO createBid(BidRequestCreate request) {
        Bid bid = new Bid();
        bid.setName(request.getName());
        bid.setDescription(request.getDescription());
        bid.setCost(request.getCost());
        bid.setRegion(request.getRegion());
        bid.setBidStatus(BidStatus.CREATED);
        bid.setTenderID(request.getTenderId());
        bid.setAuthorType(request.getAuthorType());
        bid.setAuthorId(request.getAuthorId());
        bid.setOwnerID(userService.getCurrentUser().getId());
        bid = bidRepository.save(bid);
        return BidMapper.toDto(bid);
    }

    public List<BidResponseDTO> getUserBids() {
        return (bidRepository.getUserBids(userService.getCurrentUserUsername(), AuthorType.ORGANIZATION)).stream().map(BidMapper::toDto)
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
        var bid = bidRepository.getBidsById(bidId).orElseThrow(
                () -> new NotFoundException("Could not find bid with id: " + bidId)
        );
        checkUserPermission(bid);
        throw new ForbiddenException("Could not find bid with id: " + bidId);
    }

    public BidResponseDTO editBidsStatus(UUID bidId, BidStatus bidStatus) {
        var bid = bidRepository.getBidsById(bidId).orElseThrow(
                () -> new NotFoundException("Could not find bid with id: " + bidId)
        );
        checkUserPermission(bid);
        bid.setBidStatus(bidStatus);
        return BidMapper.toDto(bidRepository.save(bid));
    }

    public BidResponseDTO editBid(UUID bidId, BidRequestEdit request) {
        var bid = bidRepository.getBidsById(bidId).orElseThrow(
                () -> new NotFoundException("Could not find bid with id: " + bidId)
        );
        checkUserPermission(bid);
        bid.setName(request.getName());
        bid.setDescription(request.getDescription());
        bid.setCost(request.getCost());
        bid.setRegion(request.getRegion());
        return BidMapper.toDto(bidRepository.save(bid));
    }

    private void checkUserPermission(Bid bid) {
        //TODO скорее всего это можно написать через JPQL и тогда можно будет сделать что-то типа Optional<Bid> getBidWithCheckPermission(UUID bidId)
        if (bid.getAuthorType() == AuthorType.EMPLOYEE && bid.getAuthorId().equals(userService.getCurrentUser().getId())){
            return;
        }
        if (bid.getAuthorType() == AuthorType.ORGANIZATION && userRepository.existsByUserAndOrganization(userService.getCurrentUserUsername(), bid.getAuthorId())) {
            return;
        }
        System.out.println(bid.getAuthorId());
        System.out.println(userService.getCurrentUser().getId());
        throw new ForbiddenException("permission denied");
    }


    public BidResponseDTO rollbackBid(@Valid @NotNull UUID bidId, @Valid @Min(1) Long version) {
        var bid = bidRepository.getBidsById(bidId).orElseThrow(
                () -> new NotFoundException("Could not find bid with id: " + bidId)
        );
        if (bid.getVersion() < version) {
            throw new IllegalArgumentException("invalid bid version");
        }
        checkUserPermission(bid);
        BidHistory bidHistory = bidHistoryRepository.findByBidIdAndVersion(bidId, version).orElseThrow(
                () -> new IllegalArgumentException("this version doesn't exist")
        );
        BidMapper.historyToEntity(bid, bidHistory);
        return BidMapper.toDto(bidRepository.save(bid));
    }
}
