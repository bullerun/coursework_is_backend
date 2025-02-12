package coursework.backend.service;


import coursework.backend.dto.TenderRequestDTO;
import coursework.backend.dto.TenderRequestEdit;
import coursework.backend.dto.TenderResponseDTO;
import coursework.backend.dto.mapper.TenderMapper;
import coursework.backend.entity.Tender;
import coursework.backend.entity.enums.EmployeePositionInOrganization;
import coursework.backend.entity.enums.TenderStatus;
import coursework.backend.exception.ForbiddenException;
import coursework.backend.exception.NotFoundException;
import coursework.backend.repository.TenderHistoryRepository;
import coursework.backend.repository.TenderRepository;
import coursework.backend.repository.UserRepository;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TenderService {

    private final TenderRepository tenderRepository;
    private final TenderHistoryRepository tenderHistoryRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    private static final NotFoundException notFoundException = new NotFoundException("not found");
    private static final ForbiddenException forbiddenException = new ForbiddenException("permission denied");

    @Transactional
    public List<TenderResponseDTO> getAllTenders(Integer page, Integer pageSize, @Pattern(regexp = "asc|desc", message = "sortDirection должен быть 'asc' или 'desc'") String sortDirection) {
        return tenderRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), "createdAt")), TenderStatus.PUBLISHED).stream().map(TenderMapper::toDto).toList();
    }

    @Transactional
    public TenderResponseDTO createTender(TenderRequestDTO request) {
        if (!userRepository.existsByUserAndOrganizationAndHasProvide(userService.getCurrentUserUsername(), request.getOrganizationId(), EmployeePositionInOrganization.HEAD)) {
            throw new ForbiddenException("you can't create tender");
        }
        Tender tender = Tender.builder()
                .name(request.getName())
                .description(request.getDescription())
                .cost(request.getCost())
                .region(request.getRegion().strip())
                .tenderStatus(TenderStatus.CREATED)
                .organizationID(request.getOrganizationId())
                .expiredAt(request.getExpiredAt())
                .ownerID(userService.getCurrentUser().getId())
                .build();
        return TenderMapper.toDto(tenderRepository.save(tender));
    }

    @Transactional
    public List<TenderResponseDTO> getUserTenders() {
        return tenderRepository.getTenderByUsersUsername(userService.getCurrentUserUsername()).stream()
                .map(TenderMapper::toDto)
                .toList();
    }

    @Transactional
    public String getTenderStatus(UUID tenderId) {
        var tender = tenderRepository.findTenderById(tenderId).orElseThrow(
                () -> notFoundException
        );
        if (tender.getTenderStatus() != TenderStatus.PUBLISHED && userRepository.invertExistsByUserAndOrganization(userService.getCurrentUserUsername(), tender.getOrganizationID())) {
            throw forbiddenException;
        }
        return tender.getTenderStatus().toString();
    }


    @Transactional
    public TenderResponseDTO updateTenderStatus(UUID tenderId, TenderStatus status) {
        Tender tender = tenderRepository.findTenderById(tenderId).orElseThrow(
                () -> notFoundException
        );

        if (userRepository.invertExistsByUserAndOrganization(userService.getCurrentUserUsername(), tender.getOrganizationID())) {
            throw forbiddenException;
        }

        tender.setTenderStatus(status);
        return TenderMapper.toDto(tenderRepository.save(tender));
    }

    @Transactional
    public TenderResponseDTO editTender(UUID tenderId, TenderRequestEdit request) {
        var tender = tenderRepository.findTenderById(tenderId).orElseThrow(
                () -> notFoundException
        );
        if (userRepository.invertExistsByUserAndOrganization(userService.getCurrentUserUsername(), tender.getOrganizationID())) {
            throw forbiddenException;
        }

        tender.setName(request.getName());
        tender.setDescription(request.getDescription());
        tender.setCost(request.getCost());
        tender.setRegion(request.getRegion().strip());

        return TenderMapper.toDto(tenderRepository.save(tender));
    }


    @Transactional
    public TenderResponseDTO rollbackTender(UUID tenderId, long version) {
        var tender = tenderRepository.findTenderById(tenderId).orElseThrow(
                () -> notFoundException
        );
        if (userRepository.invertExistsByUserAndOrganization(userService.getCurrentUserUsername(), tender.getOrganizationID())) {
            throw forbiddenException;
        }
        if (tender.getVersion() < version) {
            throw new IllegalArgumentException("this version doesn't exist");
        }
        var oldTender = tenderHistoryRepository.findByTenderIdAndVersion(tenderId, version).orElseThrow(
                () -> new IllegalArgumentException("this version doesn't exist")
        );
        TenderMapper.historyToEntity(tender, oldTender);
        return TenderMapper.toDto(tenderRepository.save(tender));
    }

}
