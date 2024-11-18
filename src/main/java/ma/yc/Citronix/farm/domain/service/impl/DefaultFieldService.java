package ma.yc.Citronix.farm.domain.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.yc.Citronix.common.domain.exception.EntityConstraintViolationException;
import ma.yc.Citronix.farm.application.dto.request.create.FieldRequestDto;
import ma.yc.Citronix.farm.application.dto.request.update.FieldUpdateDto;
import ma.yc.Citronix.farm.application.dto.response.FieldResponseDto;
import ma.yc.Citronix.farm.application.mapper.FieldMapper;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.farm.domain.service.FarmService;
import ma.yc.Citronix.farm.domain.service.FieldService;
import ma.yc.Citronix.farm.infrastructure.repository.FieldRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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

        if (farm.getFields().stream().count() >= 10) {
            throw new EntityConstraintViolationException("Farm", "Fields", farm.getFields().stream().count(), "A farm cannot have more than 10 fields.");
        }

        if (dto.surface() < 0.1) {
            throw new EntityConstraintViolationException("Field", "Surface", dto.surface(), "Field surface must be at least 0.1 hectare (1,000 m²).");
        }

        if (dto.surface() > farm.getSurface() * 0.5) {
            throw new EntityConstraintViolationException("Field", "Surface", dto.surface(), "Field surface cannot exceed 50% of the total farm surface (50% of " + farm.getSurface() + " hectares).");
        }

        double totalFieldSurface = farm.getFields().stream().map(Field::getSurface).mapToDouble(Double::doubleValue).sum();

        if (farm.getSurface() < totalFieldSurface + dto.surface()) {
            throw new EntityConstraintViolationException("Farm", "Surface", totalFieldSurface + dto.surface(), "The total surface of all fields cannot exceed the farm's surface (" + farm.getSurface() + " hectares).");
        }

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
