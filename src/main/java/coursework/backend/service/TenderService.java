package coursework.backend.service;


import coursework.backend.dto.TenderRequestDTO;
import coursework.backend.entity.Tender;
import coursework.backend.entity.TenderStatus;
import coursework.backend.repository.TenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class TenderService {

    private final TenderRepository tenderRepository;


    @Transactional
    public Tender createTender(TenderRequestDTO request) {
        Tender tender = Tender.builder()
                .name(request.getName())
                .description(request.getDescription())
                .cost(request.getCost())
                .region(request.getRegion())
                .organizationID(request.getOrganizationId())
                .tenderStatus(TenderStatus.CREATED)
                .build();
        return tenderRepository.save(tender);
    }

}
