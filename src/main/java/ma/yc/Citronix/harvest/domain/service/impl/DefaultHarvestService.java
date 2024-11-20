package ma.yc.Citronix.harvest.domain.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.service.FarmService;
import ma.yc.Citronix.harvest.application.dto.request.create.HarvestRequestDto;
import ma.yc.Citronix.harvest.application.dto.request.update.HarvestUpdateDto;
import ma.yc.Citronix.harvest.application.dto.response.HarvestResponseDto;
import ma.yc.Citronix.harvest.application.mapper.HarvestMapper;
import ma.yc.Citronix.harvest.domain.model.aggregate.Harvest;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;
import ma.yc.Citronix.harvest.domain.service.HarvestService;
import ma.yc.Citronix.harvest.infrastructure.repository.HarvestRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultHarvestService implements HarvestService {

    private final HarvestRepository repository;
    private final HarvestMapper mapper;
    private final FarmService farmService;

    @Override
    public Page<HarvestResponseDto> findAll ( int pageNum, int pageSize ) {
        return null;
    }

    @Override
    public HarvestResponseDto findById ( HarvestId id ) {
        return null;
    }

    @Override
    public HarvestResponseDto create ( HarvestRequestDto dto ) {
        Farm farm = farmService.findEntityById(dto.farm());

        Harvest harvest = Harvest.builder()
                .season(dto.season())
                .date(dto.date())
                .farm(farm)
                .build();

        return mapper.toResponseDto(repository.save(harvest));
    }

    @Override
    public HarvestResponseDto update ( HarvestId id, HarvestUpdateDto dto ) {
        return null;
    }

    @Override
    public void delete ( HarvestId id ) {

    }
}
