package ma.yc.Citronix.farm.domain.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.yc.Citronix.farm.application.dto.request.FieldRequestDto;
import ma.yc.Citronix.farm.application.dto.request.FieldUpdateDto;
import ma.yc.Citronix.farm.application.dto.response.FieldResponseDto;
import ma.yc.Citronix.farm.application.mapper.FieldMapper;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.farm.domain.service.FarmService;
import ma.yc.Citronix.farm.domain.service.FieldService;
import ma.yc.Citronix.farm.infrastructure.repository.FieldRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DefaultFieldService implements FieldService {

    private final FieldMapper mapper;
    private final FieldRepository repository;
    private final FarmService farmService;

    @Override
    public Page<FieldResponseDto> findAll ( int pageNum, int pageSize ) {
        return null;
    }

    @Override
    public FieldResponseDto findById ( Field field ) {
        return null;
    }

    @Override
    public FieldResponseDto create ( FieldRequestDto dto ) {
        Farm farm = farmService.findEntityById(dto.farm());
        if(dto.surface() < 0.1)
            throw new RuntimeException("surface sghira");

        if (dto.surface() > farm.getSurface() * 0.5)
            throw new RuntimeException("surface too long");

        if (farm.getFields().stream().count() >= 10)
            throw new RuntimeException("enough field");

        double sum = farm.getFields().stream().map(field -> field.getSurface()).mapToDouble(Double::doubleValue).sum();

        if (farm.getSurface() < sum + dto.surface())
            throw new RuntimeException("surfce kbira");

        Field field = mapper.toEntity(dto);
        field.setFarm(farm);

        return mapper.toResponseDto(repository.save(field));

    }

    @Override
    public FieldResponseDto update ( Field field, FieldUpdateDto fieldUpdateDto ) {
        return null;
    }

    @Override
    public void delete ( Field field ) {

    }

}
