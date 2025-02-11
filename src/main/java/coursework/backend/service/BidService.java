package coursework.backend.service;

import coursework.backend.dto.*;
import coursework.backend.dto.mapper.BidMapper;
import coursework.backend.dto.mapper.FeedbackMapper;
import coursework.backend.entity.Bid;
import coursework.backend.entity.BidHistory;
import coursework.backend.entity.Feedback;
import coursework.backend.entity.Organization;
import coursework.backend.entity.enums.AuthorType;
import coursework.backend.entity.enums.BidStatus;
import coursework.backend.entity.enums.FeedbackStatus;
import coursework.backend.exception.ForbiddenException;
import coursework.backend.exception.NotFoundException;
import coursework.backend.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final UserService userService;
    private final BidHistoryRepository bidHistoryRepository;
    private final FeedbackRepository feedbackRepository;
    private final OrganizationRepository organizationRepository;


    @Transactional
    public BidResponseDTO createBid(BidRequestCreate request) {

        UUID userID = null;
        Bid bid = new Bid();
        bid.setName(request.getName());
        bid.setDescription(request.getDescription());
        bid.setCost(request.getCost());
        bid.setRegion(request.getRegion());
        bid.setBidStatus(BidStatus.CREATED);
        bid.setTenderID(request.getTenderId());
        bid.setAuthorType(request.getAuthorType());
        bid.setAuthorId(request.getAuthorId());
        if (request.getAuthorType() == AuthorType.ORGANIZATION) {
            organizationRepository.findById(request.getAuthorId()).orElseThrow(
                    () -> new NotFoundException("Organization not found")
            );
        } else if (request.getAuthorType() == AuthorType.EMPLOYEE) {
            userID = userService.getCurrentUser().getId();
            bid.setAuthorId(request.getAuthorId());
        }
        if (userID == null) {
            bid.setOwnerID(userService.getCurrentUser().getId());
        } else {
            bid.setOwnerID(userID);
        }
        bid.setExpiredAt(request.getExpiredAt());
        bid = bidRepository.save(bid);
        return BidMapper.toDto(bid);
    }

    @Transactional
    public List<BidResponseDTO> getUserBids() {
        return (bidRepository.getUserBids(userService.getCurrentUserUsername(), AuthorType.ORGANIZATION)).stream().map(BidMapper::toDto)
                .toList();
    }

    @Transactional
    public List<BidResponseDTO> getBidsForTender(UUID tenderId, int page, int pageSize) {

        return bidRepository.getBidsForTender(tenderId, BidStatus.PUBLISHED, userService.getCurrentUserUsername(), PageRequest.of(page, pageSize))
                .stream()
                .map(BidMapper::toDto)
                .toList();
    }

    @Transactional
    public BidStatus getBidsStatus(UUID bidId) {
        return getBidWithPermission(bidId).getBidStatus();
    }

    @Transactional
    public BidResponseDTO editBidsStatus(UUID bidId, BidStatus bidStatus) {
        var bid = getBidWithPermission(bidId);
        bid.setBidStatus(bidStatus);
        return BidMapper.toDto(bidRepository.save(bid));
    }

    @Transactional
    public BidResponseDTO editBid(UUID bidId, BidRequestEdit request) {
        var bid = getBidWithPermission(bidId);
        bid.setName(request.getName());
        bid.setDescription(request.getDescription());
        bid.setCost(request.getCost());
        bid.setRegion(request.getRegion());
        return BidMapper.toDto(bidRepository.save(bid));
    }

    private Bid getBidWithPermission(UUID bidId) {
        return bidRepository.findBidWithPermission(bidId, userService.getCurrentUser().getId())
                .orElseThrow(() -> new ForbiddenException("Permission denied"));
    }

    @Transactional
    public BidResponseDTO rollbackBid(@Valid @NotNull UUID bidId, @Valid @Min(1) Long version) {
        var bid = getBidWithPermission(bidId);
        if (bid.getVersion() < version) {
            throw new IllegalArgumentException("invalid bid version");
        }
        BidHistory bidHistory = bidHistoryRepository.findByBidIdAndVersion(bidId, version).orElseThrow(
                () -> new IllegalArgumentException("this version doesn't exist")
        );
        BidMapper.historyToEntity(bid, bidHistory);
        return BidMapper.toDto(bidRepository.save(bid));
    }

    @Transactional
    public FeedbackResponseDTO addFeedback(FeedbackRequestDTO request) {
        Bid bid = bidRepository.findById(request.getBidId())
                .orElseThrow(() -> new NotFoundException("Bid not found"));

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        // Проверка доступа
        if (!userService.isUserInOrganization(userService.getCurrentUserUsername(), organization.getId())) {
            throw new ForbiddenException("You do not have permission to leave feedback");
        }

        Feedback feedback = Feedback.builder()
                .bid(bid)
                .organization(organization)
                .description(request.getDescription())
                .feedbackStatus(FeedbackStatus.PENDING)
                .build();

        feedback = feedbackRepository.save(feedback);
        return FeedbackMapper.toDto(feedback);
    }

    @Transactional
    public List<FeedbackResponseDTO> getFeedbacksForBid(UUID bidId) {
        return feedbackRepository.findByBidId(bidId).stream()
                .map(FeedbackMapper::toDto)
                .toList();
    }

    @Transactional
    public List<FeedbackResponseDTO> getApprovedFeedbacks(UUID bidId) {
        return feedbackRepository.findByBidIdAndFeedbackStatus(bidId, FeedbackStatus.APPROVED)
                .stream()
                .map(FeedbackMapper::toDto)
                .toList();
    }

}
