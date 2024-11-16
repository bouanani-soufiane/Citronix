package ma.yc.Citronix.farm.domain.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.yc.Citronix.farm.application.dto.request.FarmRequestDto;
import ma.yc.Citronix.farm.application.dto.response.FarmResponseDto;
import ma.yc.Citronix.farm.application.mapper.FarmMapper;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.service.FarmService;
import ma.yc.Citronix.farm.infrastructure.repository.FarmRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DefaultFarmService implements FarmService {
    private final FarmRepository repository;
    private final FarmMapper mapper;

    @Override
    public Page<FarmResponseDto> findAll ( int pageNum, int pageSize ) {
        return null;
    }

    @Override
    public FarmResponseDto findById ( Long aLong ) {
        return null;
    }

    @Override
    public FarmResponseDto create(FarmRequestDto dto) {
        return mapper.toResponseDto(repository.save(Farm.builder()
                .name(dto.name())
                .localization(dto.localization())
                .surface(dto.surface())
                .creationDate(dto.creationDate())
                .build()));
    }


    @Override
    public FarmResponseDto update ( Long aLong, FarmRequestDto farmRequestDto ) {
        return null;
    }

    @Override
    public void delete ( Long aLong ) {

    }
}
