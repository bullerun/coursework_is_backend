package coursework.backend.service;


import coursework.backend.dto.TenderRequestDTO;
import coursework.backend.dto.TenderResponseDTO;
import coursework.backend.dto.mapper.TenderMapper;
import coursework.backend.entity.Tender;
import coursework.backend.entity.enums.EmployeePositionInOrganization;
import coursework.backend.entity.enums.TenderStatus;
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


@Service
@RequiredArgsConstructor
public class TenderService {

    private final TenderRepository tenderRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    private static final NotFoundException notFoundException = new NotFoundException("user not found");
    private static final ForbiddenException forbiddenException = new ForbiddenException("permission denied");

    @Transactional
    public List<TenderResponseDTO> getTenders(Integer page, Integer pageSize, String sortDirection) {
        //TODO че тут за хуйня
//        var tenders = tenderRepository.findAll(PageRequest.of(
//                page, pageSize, Sort.by(Sort.Direction.fromString(sortDirection))));
        var tenders = tenderRepository.findAll();
        return tenders.stream().map(TenderMapper::toDto).toList();
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
        var tender = tenderRepository.getTenderById(tenderId).orElseThrow(
                () -> notFoundException
        );
        if (tender.getTenderStatus() != TenderStatus.PUBLISHED && userRepository.invertExistsByUserAndOrganization(userService.getCurrentUserUsername(), tender.getOrganizationID())) {
            throw forbiddenException;
        }
        return tender.getTenderStatus().toString();
    }


    @Transactional
    public TenderResponseDTO updateTenderStatus(UUID tenderId, TenderStatus status) {
        Tender tender = tenderRepository.getTenderById(tenderId).orElseThrow(
                () -> notFoundException
        );

        if (userRepository.invertExistsByUserAndOrganization(userService.getCurrentUserUsername(), tender.getOrganizationID())) {

            throw forbiddenException;
        }

        tender.setTenderStatus(status);
        return TenderMapper.toDto(tenderRepository.save(tender));
    }

    @Transactional
    public TenderResponseDTO editTender(UUID tenderId, TenderRequestDTO request) {
        var tender = tenderRepository.getTenderById(tenderId).orElseThrow(
                () -> notFoundException
        );
        if (userRepository.invertExistsByUserAndOrganization(userService.getCurrentUserUsername(), tender.getOrganizationID())) {
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
