package coursework.backend.service;

import coursework.backend.dto.bid.BidRequestCreate;
import coursework.backend.dto.bid.BidRequestEdit;
import coursework.backend.dto.bid.BidResponseDTO;
import coursework.backend.dto.feedback.FeedbackDecisionDTO;
import coursework.backend.dto.feedback.FeedbackRequestDTO;
import coursework.backend.dto.feedback.FeedbackResponseDTO;
import coursework.backend.dto.mapper.BidMapper;
import coursework.backend.dto.mapper.FeedbackMapper;
import coursework.backend.entity.*;
import coursework.backend.entity.enums.*;
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
    private final TenderRepository tenderRepository;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public BidResponseDTO createBid(BidRequestCreate request) {
        tenderRepository.findTenderByIdAndTenderStatus(request.getTenderId(), TenderStatus.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Tender not found")
        );
        UUID ownerId = userService.getCurrentUser().getId();

        if (request.getAuthorType() == AuthorType.ORGANIZATION) {
            organizationRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new NotFoundException("Organization not found"));
        } else if (request.getAuthorType() == AuthorType.EMPLOYEE) {
            ownerId = request.getAuthorId();
        }

        Bid bid = Bid.builder()
                .name(request.getName())
                .description(request.getDescription())
                .cost(request.getCost())
                .region(request.getRegion())
                .bidStatus(BidStatus.CREATED)
                .tenderID(request.getTenderId())
                .authorType(request.getAuthorType())
                .authorId(request.getAuthorId())
                .ownerID(ownerId)
                .expiredAt(request.getExpiredAt())
                .build();

        bid = bidRepository.save(bid);
        kafkaProducerService.sendLog("Bid created: " + bid);
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
        bid = bidRepository.save(bid);
        kafkaProducerService.sendLog("Bid updated: " + bid);
        return BidMapper.toDto(bid);
    }

    @Transactional
    public BidResponseDTO editBid(UUID bidId, BidRequestEdit request) {
        var bid = getBidWithPermission(bidId);
        bid.setName(request.getName());
        bid.setDescription(request.getDescription());
        bid.setCost(request.getCost());
        bid.setRegion(request.getRegion());
        bid = bidRepository.save(bid);
        kafkaProducerService.sendLog("Bid updated: " + bid);
        return BidMapper.toDto(bid);
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
        bid = bidRepository.save(bid);
        kafkaProducerService.sendLog("Bid version rolled back: " + bid);
        return BidMapper.toDto(bid);
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
        kafkaProducerService.sendLog("Feedback added: " + feedback);
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

    @Transactional
    public String processFeedbackDecision(UUID bidId, @Valid FeedbackDecisionDTO decision) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new IllegalArgumentException("Bid not found"));

        Tender tender = bid.getTender();

        if (decision.getDecision() == Decision.APPROVE) {
            tender.setTenderStatus(TenderStatus.CLOSED);
            tenderRepository.save(tender);

            bid.setBidStatus(BidStatus.APPROVED);
            bidRepository.save(bid);


            List<Bid> otherBids = bidRepository.findByTenderIdAndIdNot(tender.getId(), bidId);
            for (Bid otherBid : otherBids) {
                otherBid.setBidStatus(BidStatus.CLOSED);
                bidRepository.save(otherBid);
            }
        }

        return "tender has been closed";
    }
}
