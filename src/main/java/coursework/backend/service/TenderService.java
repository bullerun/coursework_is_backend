package coursework.backend.service;


import coursework.backend.dto.TenderRequestDTO;
import coursework.backend.entity.EmployeePositionInOrganization;
import coursework.backend.entity.Tender;
import coursework.backend.entity.TenderStatus;
import coursework.backend.entity.User;
import coursework.backend.exception.ForbiddenException;
import coursework.backend.exception.NotFoundException;
import coursework.backend.repository.TenderRepository;
import coursework.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TenderService {

    private final TenderRepository tenderRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public Tender createTender(TenderRequestDTO request) {
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
        return tenderRepository.save(tender);
    }

    public List<Tender> getUserTenders() {
        return tenderRepository.getTenderByUsersUsername(userService.getCurrentUserUsername());

    }

    public String getTenderStatus(UUID tenderId) {
        var tender = tenderRepository.getTenderById(tenderId).orElseThrow(
                () -> new NotFoundException("tender not found")
        );
        if (tender.getTenderStatus() == TenderStatus.CREATED || !userRepository.existsByUserAndOrganization(userService.getCurrentUserUsername(), tender.getOrganizationID())) {
            throw new ForbiddenException("permission denied");
        }
        return tender.getTenderStatus().toString();
    }


    public Tender updateTenderStatus(UUID tenderId, TenderStatus status) {
        var tender = tenderRepository.getTenderById(tenderId).orElseThrow(
                () -> new NotFoundException("tender not found")
        );
        if (!userRepository.existsByUserAndOrganization(userService.getCurrentUserUsername(), tender.getOrganizationID())) {
            throw new ForbiddenException("permission denied");
        }
        tender.setTenderStatus(status);
        return tenderRepository.save(tender);
    }

    public Tender editTender(UUID tenderId, TenderRequestDTO request) {
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
        return tenderRepository.save(newTender);
    }


    @Transactional
    public Tender rollbackTender(UUID tenderId, long version) {
        // TODO Заглушка: логика отката версии еще не реализована
        return tenderRepository.findById(tenderId)
                .orElseThrow(() -> new NotFoundException("Tender not found"));
    }
}
