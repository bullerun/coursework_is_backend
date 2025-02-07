package coursework.backend.service;


import coursework.backend.dto.TenderRequestDTO;
import coursework.backend.dto.TenderResponseDTO;
import coursework.backend.dto.mapper.TenderMapper;
import coursework.backend.entity.EmployeePositionInOrganization;
import coursework.backend.entity.Tender;
import coursework.backend.entity.TenderStatus;
import coursework.backend.exception.ForbiddenException;
import coursework.backend.exception.NotFoundException;
import coursework.backend.repository.TenderRepository;
import coursework.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TenderService {

    private final TenderRepository tenderRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public List<TenderResponseDTO> getTenders(Integer page, Integer pageSize, String sortDirection) {
        var tenders = tenderRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), "id")));
        return tenders.stream().map(TenderMapper::toDto).collect(Collectors.toList());
    }

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
                .ownerID(userService.getCurrentUser().getId())
                .build();
        return TenderMapper.toDto(tenderRepository.save(tender));
    }

    public List<TenderResponseDTO> getUserTenders() {
        return tenderRepository.getTenderByUsersUsername(userService.getCurrentUserUsername()).stream()
                .map(TenderMapper::toDto)
                .toList();


    }

    public String getTenderStatus(UUID tenderId) {
        var tender = tenderRepository.getTenderById(tenderId).orElseThrow(
                () -> new NotFoundException("tender not found")
        );
        if (tender.getTenderStatus() != TenderStatus.PUBLISHED && !userRepository.existsByUserAndOrganization(userService.getCurrentUserUsername(), tender.getOrganizationID())) {
            throw new ForbiddenException("permission denied");
        }
        return tender.getTenderStatus().toString();
    }


    public TenderResponseDTO updateTenderStatus(UUID tenderId, TenderStatus status) {
        Tender tender = tenderRepository.getTenderById(tenderId).orElseThrow(
                () -> new NotFoundException("tender not found")
        );
        if (!userRepository.existsByUserAndOrganization(userService.getCurrentUserUsername(), tender.getOrganizationID())) {
            throw new ForbiddenException("permission denied");
        }
        tender.setTenderStatus(status);
        return TenderMapper.toDto(tenderRepository.save(tender));
    }

    public TenderResponseDTO editTender(UUID tenderId, TenderRequestDTO request) {
        var tender = tenderRepository.getTenderById(tenderId).orElseThrow(
                () -> new NotFoundException("tender not found")
        );
        if (!userRepository.existsByUserAndOrganization(userService.getCurrentUserUsername(), tender.getOrganizationID())) {
            throw new ForbiddenException("permission denied");
        }
        Tender newTender = Tender.builder()
                .name(request.getName())
                .description(request.getDescription())
                .cost(request.getCost())
                .region(request.getRegion().strip())
                .tenderStatus(TenderStatus.CREATED)
                .organizationID(request.getOrganizationId())
                .ownerID(userService.getCurrentUser().getId())
                .build();
        return TenderMapper.toDto(tenderRepository.save(newTender));
    }


    @Transactional
    public TenderResponseDTO rollbackTender(UUID tenderId, long version) {
        // TODO Заглушка: логика отката версии еще не реализована
        return TenderMapper.toDto(tenderRepository.findById(tenderId)
                .orElseThrow(() -> new NotFoundException("Tender not found")));
    }

}
